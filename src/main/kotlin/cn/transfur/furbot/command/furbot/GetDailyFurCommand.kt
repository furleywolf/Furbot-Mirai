package cn.transfur.furbot.command.furbot

import cn.transfur.furbot.command.FriendAccessCommand
import cn.transfur.furbot.command.FurbotRawCommand
import cn.transfur.furbot.data.DailyFur
import cn.transfur.furbot.network.TailApiClient
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource

object GetDailyFurCommand : FurbotRawCommand(
    primaryName = "每日鉴毛",
    description = "Get Daily Fursuit from Tail API"
), FriendAccessCommand {
    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override val prefixOptional: Boolean = true

    private suspend fun getDailyFur(
        apiPath: String,
        vararg extraParameters: Pair<String, Any?>
    ): DailyFur? = TailApiClient.getFromTailApi(DailyFur.serializer(), apiPath, *extraParameters)

    override suspend fun CommandSender.onCommand(args: MessageChain) {
        if (this !is CommandSenderOnMessage<*>) {
            return
        }
        runBoth { target, _ ->
            if (args.isEmpty()) { // Random
                respond(target, getDailyFur(
                    apiPath = "api/v2/DailyFursuit/Rand"
                ))
            } else {
                val arg = args[0]
                if (arg !is PlainText) { // Other types won't be accepted
                    return@runBoth
                }
                val idOrNull = arg.content.toIntOrNull()
                if (idOrNull != null) { // By volume
                    respond(target, getDailyFur(
                        apiPath = "api/v2/DailyFursuit/id",
                        "id" to idOrNull
                    ))
                } else { // By the fur's name
                    respond(target, getDailyFur(
                        apiPath = "api/v2/DailyFursuit/name",
                        "name" to arg.content
                    ))
                }
            }
        }
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
