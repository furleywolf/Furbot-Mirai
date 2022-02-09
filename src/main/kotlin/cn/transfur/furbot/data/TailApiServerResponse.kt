package cn.transfur.furbot.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TailApiServerResponse<T>(
    val code: Int,
    @SerialName("msg")
    val message: String,
    val data: T
)