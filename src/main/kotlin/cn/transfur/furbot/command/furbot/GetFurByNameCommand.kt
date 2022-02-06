package cn.transfur.furbot.command.furbot

import cn.transfur.furbot.Config
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.FriendCommandSenderOnMessage
import net.mamoe.mirai.console.command.MemberCommandSenderOnMessage
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.buildMessageChain

object GetFurByNameCommand : GetFurCommand("来只") {
    private const val API_PATH: String = "api/v2/getFursuitByName"

    override val description: String = "Get fursuit based on name from Tail API"

    @Handler
    suspend fun CommandSenderOnMessage<*>.run(name: String) {
        when (this) {
            is FriendCommandSenderOnMessage -> {
                if (Config.furbot.respondFriends)
                    differRespond(user, name)
            }
            is MemberCommandSenderOnMessage -> {
                if (Config.furbot.respondGroups)
                    differRespond(group, name)
            }
        }
    }

    private suspend fun differRespond(target: Contact, name: String) {
        if (name != "绒狸") {
            respond(target, name)
        } else {
            respondEaster(target)
        }
    }

    private suspend fun respond(target: Contact, name: String) {
        val furPic = getFurPicSimple(API_PATH, "name" to name)

        if (furPic == null) {
            target.sendMessage("这只毛毛还没有被收录，请联系开发者添加哦~")
        } else {
            val message = buildMessageChain(3) {
                // Result text
                add("""
                    --- 每日吸毛 Bot ---
                    FurID：${furPic.fid}
                    毛毛名字：${furPic.name}
                    搜索方法：模糊
                """.trimIndent())

                // Image
                addImage(target, furPic.url)

                // Tail
                addTail()
            }

            target.sendMessage(message)
        }
    }

    private suspend fun respondEaster(target: Contact) {
        val message = buildMessageChain(3) {
            // Rdqrho m]oj
            add("""
                --- 每旤吶毘 =im ---
                FtpF@：Access Denied
                毛毚吋孔：绒狸
                搜紡斷泒：/* compiled code */
            """.trimIndent())

            // Il_da
            addImage(target, "https://q1.qlogo.cn/g?b=qq&nk=2934535422$&s=640")

            // T`gi
            addTail("--- root@FurryAir ---")
        }

        target.sendMessage(message)
    }
}