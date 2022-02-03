package cn.transfur.furbot.data

import kotlinx.serialization.Serializable

@Serializable
data class FurryPic(
    val id: Int,
    val name: String,
    val url: String
)