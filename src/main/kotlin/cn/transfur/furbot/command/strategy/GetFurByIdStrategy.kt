package cn.transfur.furbot.command.strategy

import cn.transfur.furbot.data.FurryPic
import cn.transfur.furbot.network.FurryPicClient
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.buildMessageChain

@JvmInline
value class GetFurByIdStrategy(private val id: String) : GetFurStrategy {
    override suspend fun getFur(): FurryPic? {
        return FurryPicClient.doGetFur(API_PATH, "fid" to id)
    }

    override suspend fun respond(target: Contact, furryPic: FurryPic?) {
        if (furryPic == null) {
            target.sendMessage("这只毛毛还没有被收录，请联系开发者添加哦~")
        } else {
            val message = buildMessageChain(3) {
                // Head
                add("""
                    --- 每日吸毛 Bot ---
                    FurID：${furryPic.id}
                    毛毛名字：${furryPic.name}
                    搜索方法：按 FurID 查找
                """.trimIndent())

                // Image
                addImage(target, furryPic)

                // Tail
                addTail()
            }

            target.sendMessage(message)
        }
    }

    companion object {
        private const val API_PATH = "api/v2/getFursuitByID"
    }
}