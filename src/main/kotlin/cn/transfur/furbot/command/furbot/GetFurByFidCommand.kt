package cn.transfur.furbot.command.furbot

import cn.transfur.furbot.command.SessionCommand
import cn.transfur.furbot.data.FurPic
import cn.transfur.furbot.util.sendMessage
import cn.transfur.furbot.util.sendMessageDifferently
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.contact.Contact

object GetFurByFidCommand : GetFurCommand("找毛图"), SessionCommand {
    private const val API_PATH: String = "api/v2/getFursuitByID"

    override val description: String = "Get fursuit based on fid from Tail API"

    suspend fun getFurByFid(fid: Int): FurPic? {
        return getFurPicSimple(API_PATH, "fid" to fid)
    }

    @Handler
    suspend fun CommandSenderOnMessage<*>.run() = runBoth { target, sender ->
        val fidString = sender.ask("你想找 fid 为多少的图片？") ?: return@runBoth
        val fid = fidString.toIntOrNull()
        if (fid != null) {
            respond(target, fid)
        } else {
            sender.sendMessageDifferently("这不是一个数字，或许你想用 ${GetFidsByNameCommand.primaryName} 命令？")
        }
    }

    @Handler
    suspend fun CommandSenderOnMessage<*>.run(fid: Int) = runBoth { target, _ -> respond(target, fid) }

    private suspend fun respond(target: Contact, fid: Int) {
        val furPic = getFurByFid(fid)

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