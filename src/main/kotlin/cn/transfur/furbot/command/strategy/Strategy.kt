package cn.transfur.furbot.command.strategy

import cn.transfur.furbot.Config
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.MessageChainBuilder

interface Strategy {
    suspend fun respond(target: User)

    fun MessageChainBuilder.addTail(tail: String = "--- 绒狸开源版 ---") {
        if (Config.furbot.showTail)
            add(tail)
    }
}