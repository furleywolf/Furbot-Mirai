package cn.transfur.furbot.util

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.MessageChainBuilder

suspend inline fun Contact.sendMessage(block: MessageChainBuilder.() -> Unit) {
    val message = MessageChainBuilder().apply(block).build()
    sendMessage(message)
}