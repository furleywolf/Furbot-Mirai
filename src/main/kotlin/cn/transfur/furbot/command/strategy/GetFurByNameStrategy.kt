package cn.transfur.furbot.command.strategy

import cn.transfur.furbot.Config
import cn.transfur.furbot.data.FurPic
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.buildMessageChain

@JvmInline
value class GetFurByNameStrategy(private val name: String) : GetFurStrategy {
    companion object {
        private const val API_PATH: String = "api/v2/getFursuitByName"
    }

    override suspend fun getFurPic(): FurPic? = getFurPicSimple(API_PATH, "name" to name)

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

    override suspend fun respond(target: User) {
        if (name != "绒狸") {
            super.respond(target)
        } else {
            val actualTarget = if (target is Member) target.group else target

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
                if (Config.furbot.showTail)
                    add("--- root@FurryAir ---")
            }

            actualTarget.sendMessage(message)
        }
    }
}