package cn.transfur.furbot.command.furbot

import cn.transfur.furbot.command.FurbotSimpleCommand
import cn.transfur.furbot.data.DailyFur
import cn.transfur.furbot.network.TailApiClient
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource

object GetDailyFurCommand : FurbotSimpleCommand(
    primaryName = "每日鉴毛",
    description = "Get Daily Fursuit from Tail API"
) {
    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override val prefixOptional: Boolean = true

    private suspend fun getDailyFur(
        apiPath: String,
        vararg extraParameters: Pair<String, Any?>
    ): DailyFur? = TailApiClient.getFromTailApi(DailyFur.serializer(), apiPath, *extraParameters)

    @Handler
    suspend fun CommandSenderOnMessage<*>.run() = runBoth { target, _ ->
        respond(target, getDailyFur(
            apiPath = "api/v2/DailyFursuit/Rand"
        ))
    }

    @Handler
    suspend fun CommandSenderOnMessage<*>.run(name: String) = runBoth { target, _ ->
        respond(target, getDailyFur(
            apiPath = "api/v2/DailyFursuit/name",
            "name" to name
        ))
    }

    @Handler
    suspend fun CommandSenderOnMessage<*>.run(id: Int) = runBoth { target, _ ->
        respond(target, getDailyFur(
            apiPath = "api/v2/DailyFursuit/id",
            "id" to id
        ))
    }

    private suspend fun respond(target: Contact, dailyFur: DailyFur?) {
        if (dailyFur != null) {
            TailApiClient.getImageBytes(dailyFur.url)
                .toExternalResource()
                .use { target.sendImage(it) }
        } else {
            target.sendMessage("没有找到这一期每日鉴毛呢")
        }
    }
}