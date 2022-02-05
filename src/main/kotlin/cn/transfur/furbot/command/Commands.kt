package cn.transfur.furbot.command

import cn.transfur.furbot.command.strategy.*

object Commands {
    // Get fur
    private const val GET_FUR_RAND: String = "来只毛"
    private val GET_FUR_BY_NAME: Regex = Regex("来只\\s*(.*)")
    private val GET_FUR_BY_ID: Regex = Regex("找毛图\\s*(.*)")

    // Get fid
    private val GET_FUR_ID: Regex = Regex("查fid\\s*(.*)")

    // Good night
    private const val GOOD_NIGHT: String = ".晚安"

    fun dispatch(text: String): Strategy? = when {
        GET_FUR_RAND == text -> GetFurRandStrategy
        GET_FUR_BY_NAME matches text -> GetFurByNameStrategy(GET_FUR_BY_NAME.find(text)!!.groupValues[1])
        GET_FUR_BY_ID matches text -> GetFurByIdStrategy(GET_FUR_BY_ID.find(text)!!.groupValues[1])
        GET_FUR_ID matches text -> GetFurIdStrategy(GET_FUR_ID.find(text)!!.groupValues[1])
        GOOD_NIGHT == text -> GoodNightStrategy
        else -> null
    }
}