package cn.transfur.furbot.command.strategy

import cn.transfur.furbot.data.FurPic
import cn.transfur.furbot.network.TailApiClient
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User

interface GetFurStrategy : ImageStrategy {
    suspend fun getFurPic(): FurPic?

    suspend fun getFurPicSimple(
        apiPath: String,
        vararg extraParameters: Pair<String, String>
    ): FurPic? = TailApiClient.getFromTailApi(FurPic.serializer(), apiPath, *extraParameters)

    suspend fun respond(target: Contact, furPic: FurPic?)

    override suspend fun respond(target: User) {
        val furPic = getFurPic()
        val actualTarget = if (target is Member) target.group else target
        respond(actualTarget, furPic)
    }
}