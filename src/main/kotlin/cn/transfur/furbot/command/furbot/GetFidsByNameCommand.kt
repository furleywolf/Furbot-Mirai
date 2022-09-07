package cn.transfur.furbot.command.furbot

import cn.transfur.furbot.command.FriendAccessCommand
import cn.transfur.furbot.command.FurbotSimpleCommand
import cn.transfur.furbot.command.SessionCommand
import cn.transfur.furbot.command.TailApiAwareCommand
import cn.transfur.furbot.data.Fids
import cn.transfur.furbot.util.sendMessage
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact

object GetFidsByNameCommand : FurbotSimpleCommand(
    primaryName = "查fid",
    description = "Get fids based on name from Tail API"
), SessionCommand, TailApiAwareCommand, FriendAccessCommand {
    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override val prefixOptional: Boolean = true

    override val apiPath: String get() = "api/v2/getFursuitFid"

    suspend fun get(name: String): Fids { // Never 404, instead returns empty list
        return getFromTailApi(Fids.serializer(), "name" to name)!!
    }

    @Handler
    suspend fun CommandSenderOnMessage<*>.run() = runBoth { target, sender ->
        val name = sender.ask("你想查哪只毛毛？") ?: return@runBoth
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
        val fids = get(name)

        if (fids.isEmpty()) {
            target.sendMessage("这只毛毛还没有被收录，请联系开发者添加哦~")
        } else {
            target.sendMessage {
                // Result text
                add("搜索结果：${fids.joinToString(separator = "、")}\n")

                // Tail
                addTail()
            }
        }
    }

    private suspend fun respondEaster(target: Contact) {
        target.sendMessage {
            // Rdqrho m]oj
            add("搜紡绑枙：Access Denied\n")

            // T`gi
            addTail("--- root@FurryAir ---")
        }
    }
}