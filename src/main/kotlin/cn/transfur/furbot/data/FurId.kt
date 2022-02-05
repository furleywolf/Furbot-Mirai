package cn.transfur.furbot.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FurId(
    @SerialName("fids")
    val ids: List<Int>
)