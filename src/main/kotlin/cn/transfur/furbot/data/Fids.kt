package cn.transfur.furbot.data

import kotlinx.serialization.Serializable

@Serializable
data class Fids(
    val fids: List<Int>
) : List<Int> by fids