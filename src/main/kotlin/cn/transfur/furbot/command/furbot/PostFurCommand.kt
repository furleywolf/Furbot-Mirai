package cn.transfur.furbot.command.furbot

import cn.transfur.furbot.command.FriendAccessCommand
import cn.transfur.furbot.command.FurbotSimpleCommand
import cn.transfur.furbot.network.TailApiClient
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource

object PostFurCommand : FurbotSimpleCommand(
    primaryName = "投只毛",
    description = "Show qq miniprogram for post furs"
), FriendAccessCommand {
    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override val prefixOptional: Boolean = true

    @Handler
    suspend fun CommandSenderOnMessage<*>.run() = runBoth { target, _ -> respond(target) }

    private suspend fun respond(target: Contact) {
        target.sendMessage("请前往绒狸小程序投稿")
        TailApiClient.getImageBytes("https://s2.loli.net/2022/04/12/kpaO594er6RXQ7D.jpg")
            .toExternalResource()
            .use { resource -> target.sendImage(resource) }
    }
}