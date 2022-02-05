package cn.transfur.furbot.command.strategy

import cn.transfur.furbot.Config
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.MessageChainBuilder

interface Strategy {
    suspend fun respond(target: User)

    fun MessageChainBuilder.addTail() {
        if (Config.furbot.showTail)
            add("--- 绒狸开源版 ---")
    }
}