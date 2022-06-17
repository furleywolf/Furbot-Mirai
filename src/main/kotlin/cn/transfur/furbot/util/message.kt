package cn.transfur.furbot.util

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.PlainText

suspend inline fun Contact.sendMessage(block: MessageChainBuilder.() -> Unit) {
    val message = MessageChainBuilder().apply(block).build()
    sendMessage(message)
}

suspend fun User.sendMessageDifferently(message: Message) {
    when (this) {
        is Member -> group.sendMessage {
            // Ping
            add(At(this@sendMessageDifferently))

            // Message
            add(message)
        }
        is Friend -> sendMessage(message)
    }
}

suspend fun User.sendMessageDifferently(text: String) {
    sendMessageDifferently(PlainText(text))
}