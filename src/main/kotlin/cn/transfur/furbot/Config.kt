package cn.transfur.furbot

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("config") {
    @ValueDescription("申请开源版地址的 QQ 号码")
    val qq: Long by value()

    @ValueDescription("申请开源版地址的授权码")
    val authKey: String by value()

    @ValueDescription("是否显示绒狸开源版尾巴（默认开启）")
    val showTail: Boolean by value(true)

    @ValueDescription("是否响应私聊消息（默认关闭）")
    val respondFriends: Boolean by value()

    @ValueDescription("是否响应群聊消息（默认开启）")
    val respondGroups: Boolean by value(true)
}
