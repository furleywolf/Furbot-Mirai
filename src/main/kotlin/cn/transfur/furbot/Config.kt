package cn.transfur.furbot

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import net.mamoe.yamlkt.Comment

object Config : AutoSavePluginConfig("config") {
    @ValueDescription("与吸毛模块相关的配置")
    val furbot: Furbot by value()

    @Serializable
    data class Furbot(
        @Comment("申请开源版地址的 QQ 号码")
        val qq: Long = 0L,
        @Comment("申请开源版地址的授权码")
        val authKey: String = "",
        @Comment("是否显示绒狸开源版尾巴（默认开启）")
        val showTail: Boolean = true,
        @Comment("是否响应私聊消息（默认关闭）")
        val respondFriends: Boolean = false,
        @Comment("是否响应群聊消息（默认开启）")
        val respondGroups: Boolean = true,
    )

    @ValueDescription(".晚安相关配置")
    val goodNight: GoodNight by value()

    @Serializable
    data class GoodNight(
        @Comment("最早睡觉时间，以 hh:MM:ss 的形式表示，24 小时制")
        val startTime: String = "20:00:00",
        @Comment("最晚睡觉时间，以 hh:MM:ss 的形式表示，24 小时制")
        val endTime: String = "07:00:00"
    )
}
