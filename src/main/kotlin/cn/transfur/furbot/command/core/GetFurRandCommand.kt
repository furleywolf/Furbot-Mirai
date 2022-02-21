package cn.transfur.furbot.command.core

import cn.transfur.furbot.data.FurPic
import cn.transfur.furbot.util.sendMessage
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.contact.Contact

object GetFurRandCommand : GetFurCommand("来只毛") {
    private const val API_PATH: String = "api/v2/getFursuitRand"

    override val description: String = "Get random fursuit from Tail API"

    suspend fun getFurRand(): FurPic? {
        return getFurPicSimple(API_PATH)
    }

    @Handler
    suspend fun CommandSenderOnMessage<*>.run() = runBoth { target, _ -> respond(target) }

    private suspend fun respond(target: Contact) {
        val furPic = getFurRand() ?: return // Swallow here

        target.sendMessage {
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
    }
}