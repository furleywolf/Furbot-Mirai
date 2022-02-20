package cn.transfur.furbot.command

import cn.transfur.furbot.network.TailApiClient
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource

interface ImageAwareCommand : Command {
    suspend fun MessageChainBuilder.addImage(target: Contact, urlString: String) {
        TailApiClient.getImageBytes(urlString)
            .toExternalResource()
            .use { resource -> add(target.uploadImage(resource)) }
    }
}