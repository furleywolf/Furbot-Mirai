package cn.transfur.furbot.command.furbot

import cn.transfur.furbot.command.SessionCommand
import cn.transfur.furbot.data.FurPic
import cn.transfur.furbot.util.sendMessage
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.contact.Contact

object GetFurByFidCommand : GetFurCommand(
    apiPath = "api/v2/getFursuitByID",
    primaryName = "找毛图",
    description = "Get fursuit based on fid from Tail API"
), SessionCommand {
    suspend fun get(fid: String): FurPic? {
        return getFurPic("fid" to fid)
    }

    @Handler
    suspend fun CommandSenderOnMessage<*>.run() = runBoth { target, sender ->
        val fid = sender.ask("你想找 fid 是什么的图片？") ?: return@runBoth
        respond(target, fid)
    }

    @Handler
    suspend fun CommandSenderOnMessage<*>.run(fid: String) = runBoth { target, _ -> respond(target, fid) }

    private suspend fun respond(target: Contact, fid: String) {
        val furPic = get(fid)

        if (furPic == null) {
            target.sendMessage("这只毛毛还没有被收录，请联系开发者添加哦~")
        } else {
            target.sendMessage {
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
        }
    }
}