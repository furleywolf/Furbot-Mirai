package cn.transfur.furbot.command.strategy

import cn.transfur.furbot.data.FurPic
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.buildMessageChain

@JvmInline
value class GetFurByIdStrategy(private val id: String) : GetFurStrategy {
    companion object {
        private const val API_PATH: String = "api/v2/getFursuitByID"
    }

    override suspend fun getFurPic(): FurPic? = getFurPicSimple(API_PATH, "fid" to id)

    override suspend fun respond(target: Contact, furPic: FurPic?) {
        if (furPic == null) {
            target.sendMessage("这只毛毛还没有被收录，请联系开发者添加哦~")
        } else {
            val message = buildMessageChain(3) {
                // Result text
                add("""
                    --- 每日吸毛 Bot ---
                    FurID：${furPic.id}
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