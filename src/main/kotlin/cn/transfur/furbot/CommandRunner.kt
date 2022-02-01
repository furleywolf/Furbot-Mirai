package cn.transfur.furbot

import cn.transfur.furbot.strategy.GetFurByIdStrategy
import cn.transfur.furbot.strategy.GetFurByNameStrategy
import cn.transfur.furbot.strategy.GetFurRandStrategy
import net.mamoe.mirai.event.events.GroupEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText

object CommandRunner {
    private const val GET_RANDOM_FUR: String = "来只毛"
    private val GET_FUR_BY_NAME: Regex = Regex("来只\\s*(.*)")
    private val GET_FUR_BY_ID: Regex = Regex("找毛图\\s*(.*)")

    suspend fun run(messageEvent: MessageEvent) {
        val text = getFirstPlainText(messageEvent.message)?.trim() ?: return

        val strategy = when {
            GET_RANDOM_FUR == text -> GetFurRandStrategy
            GET_FUR_BY_NAME matches text -> GetFurByNameStrategy(GET_FUR_BY_NAME.find(text)!!.groupValues[1])
            GET_FUR_BY_ID matches text -> GetFurByIdStrategy(GET_FUR_BY_ID.find(text)!!.groupValues[1])
            else -> return
        }

        val target = if (messageEvent is GroupEvent) messageEvent.group else messageEvent.sender

        with(strategy) {
            val furryPic = getFur()
            respond(target, furryPic)
        }
    }

    private fun getFirstPlainText(messageChain: MessageChain): String? {
        for (message in messageChain) {
            if (message is PlainText)
                return message.content
        }
        return null
    }
}