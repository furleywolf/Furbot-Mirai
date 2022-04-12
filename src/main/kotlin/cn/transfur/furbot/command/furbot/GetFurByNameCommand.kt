package cn.transfur.furbot.command.furbot

import cn.transfur.furbot.command.SessionCommand
import cn.transfur.furbot.data.FurPic
import cn.transfur.furbot.util.sendMessage
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.contact.Contact

object GetFurByNameCommand : GetFurCommand(
    apiPath = "api/v2/getFursuitByName",
    primaryName = "来只",
    description = "Get fursuit based on name from Tail API"
), SessionCommand {
    suspend fun get(name: String): FurPic? {
        return getFurPic("name" to name)
    }

    @Handler
    suspend fun CommandSenderOnMessage<*>.run() = runBoth { target, sender ->
        val name = sender.ask("你想来只谁？") ?: return@runBoth
        differRespond(target, name)
    }

    @Handler
    suspend fun CommandSenderOnMessage<*>.run(name: String) = runBoth { target, _ -> differRespond(target, name) }

    private suspend fun differRespond(target: Contact, name: String) {
        if (name != "绒狸") {
            respond(target, name)
        } else {
            respondEaster(target)
        }
    }

    private suspend fun respond(target: Contact, name: String) {
        val furPic = get(name)

        if (furPic == null) {
            target.sendMessage("这只毛毛还没有被收录，请联系开发者添加哦~")
        } else {
            target.sendMessage {
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
        }
    }

    private suspend fun respondEaster(target: Contact) {
        target.sendMessage {
            // Rdqrho m]oj
            add("""
                --- 每旤吶毘 =im ---
                FtpF@：Access Denied
                毛毚吋孔：绒狸
                搜紡斷泒：/* compiled code */
            """.trimIndent())

            // Il_da
            addImage(target, "https://cdn.furryfan.cn/bot/avatar.png")

            // T`gi
            addTail("--- root@FurryAir ---")
        }
    }
}
