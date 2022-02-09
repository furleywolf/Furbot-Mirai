package cn.transfur.furbot.command.furbot

import cn.transfur.furbot.Config
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.FriendCommandSenderOnMessage
import net.mamoe.mirai.console.command.MemberCommandSenderOnMessage
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.buildMessageChain

object GetFurRandCommand : GetFurCommand("来只毛") {
    private const val API_PATH: String = "api/v2/getFursuitRand"

    override val description: String = "Get random fursuit from Tail API"

    @Handler
    suspend fun CommandSenderOnMessage<*>.run() {
        when (this) {
            is FriendCommandSenderOnMessage -> {
                if (Config.furbot.respondFriends)
                    respond(user)
            }
            is MemberCommandSenderOnMessage -> {
                if (Config.furbot.respondGroups)
                    respond(group)
            }
        }
    }

    private suspend fun respond(target: Contact) {
        val furPic = getFurPicSimple(API_PATH) ?: return // Swallow here

        val message = buildMessageChain(3) {
            // Result text
            add("""
                --- 每日吸毛 Bot ---
                今天你吸毛了嘛？
                FurID：${furPic.fid}
                毛毛名字：${furPic.name}
                搜索方法：全局随机
            """.trimIndent())

            // Image
            addImage(target, furPic.url)

            // Tail
            addTail()
        }

        target.sendMessage(message)
    }
}