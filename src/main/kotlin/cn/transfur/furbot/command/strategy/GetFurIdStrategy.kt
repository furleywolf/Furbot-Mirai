package cn.transfur.furbot.command.strategy

import cn.transfur.furbot.data.FurId
import cn.transfur.furbot.network.TailApiClient
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.buildMessageChain

@JvmInline
value class GetFurIdStrategy(private val name: String) : Strategy {
    companion object {
        private const val API_PATH: String = "api/v2/getFursuitFid"
    }

    private suspend fun getFurId(): FurId { // Never 404, instead returns empty list
        return TailApiClient.getFromTailApi(FurId.serializer(), API_PATH, "name" to name)!!
    }

    override suspend fun respond(target: User) {
        val furId = getFurId()

        if (furId.ids.isEmpty()) {
            target.sendMessage("这只毛毛还没有被收录，请联系开发者添加哦~")
        } else {
            val actualTarget = if (target is Member) target.group else target

            val message = buildMessageChain(2) {
                // Result text
                add("搜索结果：${furId.ids.joinToString(separator = "、")}")

                // Tail
                addTail()
            }

            actualTarget.sendMessage(message)
        }
    }
}