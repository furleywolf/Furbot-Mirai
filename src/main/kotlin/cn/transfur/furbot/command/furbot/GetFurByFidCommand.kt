package cn.transfur.furbot.command.furbot

import cn.transfur.furbot.Config
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.FriendCommandSenderOnMessage
import net.mamoe.mirai.console.command.MemberCommandSenderOnMessage
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.buildMessageChain

object GetFurByFidCommand : GetFurCommand("找毛图") {
    private const val API_PATH: String = "api/v2/getFursuitByID"

    override val description: String = "Get fursuit based on fid from Tail API"

    @Handler
    suspend fun CommandSenderOnMessage<*>.run(fid: Int) {
        when (this) {
            is FriendCommandSenderOnMessage -> {
                if (Config.furbot.respondFriends)
                    respond(user, fid)
            }
            is MemberCommandSenderOnMessage -> {
                if (Config.furbot.respondGroups)
                    respond(group, fid)
            }
        }
    }

    private suspend fun respond(target: Contact, fid: Int) {
        val furPic = getFurPicSimple(API_PATH, "fid" to fid)

        if (furPic == null) {
            target.sendMessage("这只毛毛还没有被收录，请联系开发者添加哦~")
        } else {
            val message = buildMessageChain(3) {
                // Result text
                add("""
                    --- 每日吸毛 Bot ---
                    FurID：${furPic.fid}
                    毛毛名字：${furPic.name}
                    搜索方法：按 FurID 查找
                """.trimIndent())

                // Image
                addImage(target, furPic.url)

                // Tail
                addTail()
            }

            target.sendMessage(message)
        }
    }
}