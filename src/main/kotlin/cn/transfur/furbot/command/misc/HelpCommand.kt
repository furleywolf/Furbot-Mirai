package cn.transfur.furbot.command.misc

import cn.transfur.furbot.command.FurbotSimpleCommand
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.contact.Contact

object HelpCommand : FurbotSimpleCommand("帮助") {
    override val description: String = "Get command help"

    @Handler
    suspend fun CommandSenderOnMessage<*>.run() = differContact { respond(it) }

    private suspend fun respond(target: Contact) {
        // Note: there are several differences between the original commands
        // and these in Furbot-Mirai, like session support. For now just use
        // the original instructions.
        target.sendMessage("命令说明：https://www.kancloud.cn/furleywolf/furbot/2482928")
    }
}