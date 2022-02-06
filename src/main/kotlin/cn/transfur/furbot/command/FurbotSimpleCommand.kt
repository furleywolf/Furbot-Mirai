package cn.transfur.furbot.command

import cn.transfur.furbot.Config
import cn.transfur.furbot.KotlinPluginMain
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.message.data.MessageChainBuilder

abstract class FurbotSimpleCommand(primaryName: String) : SimpleCommand(KotlinPluginMain, primaryName) {
    fun MessageChainBuilder.addTail(tail: String = "--- 绒狸开源版 ---") {
        if (Config.furbot.showTail)
            add(tail)
    }
}