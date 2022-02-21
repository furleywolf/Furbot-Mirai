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
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource

interface FurbotCommand : Command {
    fun MessageChainBuilder.addTail(tail: String = "--- 绒狸开源版 ---") {
        if (Config.furbot.showTail)
            add(tail)
    }
}

interface SessionCommand : Command {
    /**
     * 以 [指定问题][question] 询问 [该用户][this]，期望获得文本回复.
     */
    suspend fun User.ask(question: String, timeout: Int = 30): String? {
        val answer = askImpl<PlainText>(question, timeout)
        return answer?.content
    }

    /**
     * 询问 [该用户][this]，期望获得图片 URL.
     */
    suspend fun User.askForImage(timeout: Int = 30): String? {
        val answer = askImpl<Image>("请发送一张图片", timeout)
        return answer?.queryUrl()
    }

    private suspend inline fun <reified R : SingleMessage> User.askImpl(
        question: String,
        timeout: Int = 30
    ): R? = when (this) {
        is Member -> {
            group.sendMessage {
                // Ping
                add(At(this@askImpl))

                // Question
                add(" $question")
            }

            this.listen<GroupMessageEvent, R>(timeout).also { answer ->
                if (answer == null) {
                    group.sendMessage {
                        // Ping
                        add(At(this@askImpl))

                        // Question
                        add(" 回复太慢啦，我只听 $timeout 秒")
                    }
                }
            }
        }
        is Friend -> {
            sendMessage(question)

            this.listen<FriendMessageEvent, R>(timeout).also { answer ->
                if (answer == null)
                    sendMessage("回复太慢啦，我只听 $timeout 秒")
            }
        }
        else -> error("Unsupported operation from Temp $id")
    }

    private suspend inline fun <reified E : MessageEvent, reified R : SingleMessage> User.listen(
        timeout: Int = 30
    ): R? = nextEventOrNull<E>(1000L * timeout) { event ->
        event.sender.id == this.id && event.message.firstIsInstanceOrNull<R>() != null
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