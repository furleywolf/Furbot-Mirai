package cn.transfur.furbot.strategy

import cn.transfur.furbot.data.FurryPic
import cn.transfur.furbot.network.FurryPicClient
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.buildMessageChain

object GetFurRandStrategy : GetFurStrategy {
    private const val API_PATH = "api/v2/getFursuitRand"

    override suspend fun getFur(): FurryPic? {
        return FurryPicClient.doGetFur(API_PATH)
    }

    override suspend fun respond(target: Contact, furryPic: FurryPic?) {
        if (furryPic == null) return // Swallow here

        val message = buildMessageChain(3) {
            // Head
            add("""
                --- 每日吸毛 Bot ---
                今天你吸毛了嘛？
                FurID：${furryPic.id}
                毛毛名字：${furryPic.name}
                搜索方法：全局随机
            """.trimIndent())

            // Image
            addImage(target, furryPic)

            // Tail
            addTail()
        }

        target.sendMessage(message)
    }
}