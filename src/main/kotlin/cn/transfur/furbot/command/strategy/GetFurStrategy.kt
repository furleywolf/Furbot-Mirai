package cn.transfur.furbot.command.strategy

import cn.transfur.furbot.Config
import cn.transfur.furbot.data.FurryPic
import cn.transfur.furbot.network.FurryPicClient
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource

interface GetFurStrategy : Strategy {
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

    override suspend fun respond(target: User) {
        val furryPic = getFur()
        val actualTarget = if (target is Member) target.group else target
        respond(actualTarget, furryPic)
    }
}