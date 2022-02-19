package cn.transfur.furbot.command.misc

import cn.transfur.furbot.command.FurbotSimpleCommand
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.contact.Contact

object HelpCommand : FurbotSimpleCommand("帮助") {
    override val description: String = "Get command help"

    @Handler
    suspend fun CommandSenderOnMessage<*>.run() = differContact { respond(it) }

    private suspend fun respond(target: Contact) {
        target.sendMessage("命令说明正在撰写")
    }
}