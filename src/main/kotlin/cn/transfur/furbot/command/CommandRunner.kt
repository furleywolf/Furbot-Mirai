package cn.transfur.furbot.command

import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.content

object CommandRunner {
    suspend fun run(messageEvent: MessageEvent) {
        val text = messageEvent.message.firstOrNull { it is PlainText }?.content ?: return

        val strategy = Commands.dispatch(text)

        strategy?.respond(messageEvent.sender)
    }
}