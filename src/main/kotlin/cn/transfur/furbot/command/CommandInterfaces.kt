package cn.transfur.furbot.command

import cn.transfur.furbot.Config
import cn.transfur.furbot.network.TailApiClient
import cn.transfur.furbot.util.sendMessageDifferently
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.FriendCommandSenderOnMessage
import net.mamoe.mirai.console.command.MemberCommandSenderOnMessage
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.event.nextEventOrNull
import net.mamoe.mirai.message.data.firstIsInstance
import net.mamoe.mirai.message.data.firstIsInstanceOrNull
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.SingleMessage
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource

interface FurbotCommand : Command {
    fun MessageChainBuilder.addTail(tail: String = "--- 绒狸开源版 ---") {
        if (Config.global.showTail)
            add(tail)
    }

    suspend fun MemberCommandSenderOnMessage.run(action: suspend (group: Group, sender: Member) -> Unit) {
        if (Config.global.respondGroups)
            action(group, user)
    }
}

interface SessionCommand : Command {
    /**
     * 以 [指定问题][question] 询问 [该用户][this]
     *
     * @return 文本回复
     */
    suspend fun User.ask(question: String, timeout: Int = 30): String? {
        sendMessageDifferently(question)
        val answer = listen<PlainText>(timeout)
        return answer?.content
    }

    /**
     * 以 [指定问题][question] 请求 [该用户][this] 发送一张 [图片][Image]
     *
     * @return 图片 URL
     */
    suspend fun User.askForImage(question: String, timeout: Int = 30): String? {
        sendMessageDifferently(question)
        val answer = listen<Image>(timeout)
        return answer?.queryUrl()
    }

    private suspend inline fun <reified R : SingleMessage> User.listen(
        timeout: Int = 30
    ): R? = when (this) {
        is Member -> listenImpl<GroupMessageEvent, R>(timeout)
        is Friend -> listenImpl<FriendMessageEvent, R>(timeout)
        else -> error("Unsupported operation from Temp $id")
    }.also { answer ->
        if (answer == null)
            sendMessageDifferently("回复太慢啦，我只听 $timeout 秒")
    }

    private suspend inline fun <reified E : MessageEvent, reified R : SingleMessage> User.listenImpl(
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

interface FriendAccessCommand : FurbotCommand {
    suspend fun CommandSenderOnMessage<*>.runBoth(action: suspend (target: Contact, sender: User) -> Unit) {
        when (this) {
            is MemberCommandSenderOnMessage -> run(action)
            is FriendCommandSenderOnMessage -> run(action)
        }
    }

    suspend fun FriendCommandSenderOnMessage.run(action: suspend (friend: Friend, sender: Friend) -> Unit) {
        if (Config.global.respondFriends)
            action(user, user)
    }
}