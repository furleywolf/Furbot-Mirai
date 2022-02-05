package cn.transfur.furbot.command.strategy

import cn.transfur.furbot.data.FurPic
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.buildMessageChain

object GetFurRandStrategy : GetFurStrategy {
    private const val API_PATH: String = "api/v2/getFursuitRand"

    override suspend fun getFurPic(): FurPic? = getFurPicSimple(API_PATH)

    override suspend fun respond(target: Contact, furPic: FurPic?) {
        if (furPic == null) return // Swallow here

        val message = buildMessageChain(3) {
            // Result text
            add("""
                --- 每日吸毛 Bot ---
                今天你吸毛了嘛？
                FurID：${furPic.id}
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