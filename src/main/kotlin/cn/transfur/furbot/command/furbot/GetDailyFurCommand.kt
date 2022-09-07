package cn.transfur.furbot.command.furbot

import cn.transfur.furbot.command.FriendAccessCommand
import cn.transfur.furbot.command.FurbotRawCommand
import cn.transfur.furbot.command.ImageAwareCommand
import cn.transfur.furbot.data.DailyFur
import cn.transfur.furbot.network.TailApiClient
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText

object GetDailyFurCommand : FurbotRawCommand(
    primaryName = "每日鉴毛",
    description = "Get Daily Fursuit from Tail API"
), FriendAccessCommand, ImageAwareCommand {
    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override val prefixOptional: Boolean = true

    suspend fun get(): DailyFur = getDailyFur("api/v2/DailyFursuit/Rand")!!

    suspend fun get(id: Int): DailyFur? = getDailyFur("api/v2/DailyFursuit/id", "id" to id)

    suspend fun get(name: String): DailyFur? = getDailyFur("api/v2/DailyFursuit/name", "name" to name)

    private suspend fun getDailyFur(
        apiPath: String,
        vararg extraParameters: Pair<String, Any?>
    ): DailyFur? = TailApiClient.getFromTailApi(DailyFur.serializer(), apiPath, *extraParameters)

    override suspend fun CommandSender.onCommand(args: MessageChain) {
        if (this !is CommandSenderOnMessage<*>)
            return
        runBoth { target, _ ->
            val dailyFur = if (args.isEmpty()) { // Random
                get()
            } else {
                val arg = args[0]
                if (arg !is PlainText)
                    return@runBoth // Other types won't be accepted

                val idOrNull = arg.content.toIntOrNull()
                if (idOrNull != null) { // By volume
                    get(idOrNull)
                } else { // By name
                    get(arg.content)
                }
            }

            respond(target, dailyFur)
        }
    }

    private suspend fun respond(target: Contact, dailyFur: DailyFur?) {
        if (dailyFur != null) {
            target.sendImage(dailyFur.url)
        } else {
            target.sendMessage("没有找到这一期每日鉴毛呢")
        }
    }
}
