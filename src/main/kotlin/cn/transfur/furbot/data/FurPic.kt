package cn.transfur.furbot.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FurPic(
    @SerialName("id")
    val fid: Int,
    val cid: String?,
    val name: String,
    val url: String
)