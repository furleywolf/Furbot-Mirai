package cn.transfur.furbot.command.misc

import cn.transfur.furbot.command.FurbotSimpleCommand
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.Face
import net.mamoe.mirai.message.data.buildMessageChain

object HelpCommand : FurbotSimpleCommand("帮助") {
    override val description: String = "Get command help"

    @Handler
    suspend fun CommandSenderOnMessage<*>.run() = differContact { respond(it) }

    private suspend fun respond(target: Contact) {
        val message = buildMessageChain(5) {
            // Title
            add("--- 每日吸毛 Bot ---\n")
            add(Face(Face.JU_HUA))
            add("快乐吸毛每一天")
            add(Face(Face.JU_HUA))
            add("\n")

            // Instructions link
            add("--- 命令说明 ---\n")

            // Note: there are several differences between the original commands
            // and these in Furbot-Mirai, like session support. For now just use
            // the original instructions.
            add("https://www.kancloud.cn/furleywolf/furbot/2482928")
        }

        target.sendMessage(message)
    }
}