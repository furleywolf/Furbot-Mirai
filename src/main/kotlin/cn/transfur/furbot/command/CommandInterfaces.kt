package cn.transfur.furbot.command

import cn.transfur.furbot.Config
import cn.transfur.furbot.network.TailApiClient
import cn.transfur.furbot.util.sendMessage
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.nextEventOrNull
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.firstIsInstance
import net.mamoe.mirai.message.data.firstIsInstanceOrNull
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.SingleMessage
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource

interface FurbotCommand : Command {
    fun MessageChainBuilder.addTail(tail: String = "--- 绒狸开源版 ---") {
        if (Config.furbot.showTail)
            add(tail)
    }
}

interface SessionCommand : Command {
    suspend fun User.ask(question: String, timeout: Int = 30): String? = when (this) {
        is Member -> {
            group.sendMessage {
                // Ping
                add(At(this@ask))

                // Question
                add(" $question")
            }

            val answer = listen<GroupMessageEvent, PlainText>(timeout)

            if (answer == null) {
                group.sendMessage {
                    // Ping
                    add(At(this@ask))

                    // Question
                    add(" 回复太慢啦，我只听 $timeout 秒")
                }
            }

            answer?.content
        }
        is Friend -> {
            sendMessage(question)

            val answer = listen<FriendMessageEvent, PlainText>(timeout)

            if (answer == null)
                sendMessage("回复太慢啦，我只听 $timeout 秒")

            answer?.content
        }
        else -> error("Unsupported operation from Temp $id")
    }

    private suspend inline fun <reified E : MessageEvent, reified R : SingleMessage> User.listen(
        timeout: Int = 30
    ): R? = nextEventOrNull<E>(1000L * timeout) {
        it.sender.id == id && it.message.firstIsInstanceOrNull<R>() != null
    }?.message?.firstIsInstance()
}

interface ImageAwareCommand : Command {
    suspend fun MessageChainBuilder.addImage(target: Contact, urlString: String) {
        TailApiClient.getImageBytes(urlString)
            .toExternalResource()
            .use { resource -> add(target.uploadImage(resource)) }
    }
}

interface GroupOnlyCommand : Command