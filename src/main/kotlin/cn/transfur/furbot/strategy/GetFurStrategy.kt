package cn.transfur.furbot.strategy

import cn.transfur.furbot.Config
import cn.transfur.furbot.data.FurryPic
import cn.transfur.furbot.network.FurryPicClient
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource

interface GetFurStrategy {
    suspend fun getFur(): FurryPic?

    suspend fun respond(target: Contact, furryPic: FurryPic?)

    suspend fun MessageChainBuilder.addImage(target: Contact, furryPic: FurryPic) {
        FurryPicClient.doGetImageBytes(furryPic.url)
            .toExternalResource()
            .use { resource -> add(target.uploadImage(resource)) }
    }

    fun MessageChainBuilder.addTail() {
        if (Config.furbot.showTail)
            add("--- 绒狸开源版 ---")
    }
}