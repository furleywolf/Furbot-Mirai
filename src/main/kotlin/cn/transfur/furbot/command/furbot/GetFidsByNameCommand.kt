package cn.transfur.furbot.command.furbot

import cn.transfur.furbot.Config
import cn.transfur.furbot.command.FurbotSimpleCommand
import cn.transfur.furbot.data.Fids
import cn.transfur.furbot.network.TailApiClient
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.FriendCommandSenderOnMessage
import net.mamoe.mirai.console.command.MemberCommandSenderOnMessage
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.buildMessageChain

object GetFidsByNameCommand : FurbotSimpleCommand("查fid") {
    private const val API_PATH: String = "api/v2/getFursuitFid"

    override val description: String = "Get fids based on name from Tail API"

    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override val prefixOptional: Boolean = true

    private suspend fun getFids(name: String): Fids { // Never 404, instead returns empty list
        return TailApiClient.getFromTailApi(Fids.serializer(), API_PATH, "name" to name)!!
    }

    @Handler
    suspend fun CommandSenderOnMessage<*>.run(name: String) {
        when (this) {
            is FriendCommandSenderOnMessage -> {
                if (Config.furbot.respondFriends)
                    differRespond(user, name)
            }
            is MemberCommandSenderOnMessage -> {
                if (Config.furbot.respondGroups)
                    differRespond(group, name)
            }
        }
    }

    private suspend fun differRespond(target: Contact, name: String) {
        if (name != "绒狸") {
            respond(target, name)
        } else {
            respondEaster(target)
        }
    }

    private suspend fun respond(target: Contact, name: String) {
        val fids = getFids(name)

        if (fids.isEmpty()) {
            target.sendMessage("这只毛毛还没有被收录，请联系开发者添加哦~")
        } else {
            val message = buildMessageChain(1) {
                // Result text
                add("搜索结果：${fids.joinToString(separator = "、")}\n")

                // Tail
                addTail()
            }

            target.sendMessage(message)
        }
    }

    private suspend fun respondEaster(target: Contact) {
        val message = buildMessageChain(1) {
            // Result text
            add("搜紡绑枙：Access Denied\n")

            // Tail
            addTail("--- root@FurryAir ---")
        }

        target.sendMessage(message)
    }
}