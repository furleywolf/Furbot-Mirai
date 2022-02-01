package cn.transfur.furbot

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("furbot-config") {
    @ValueDescription("申请开源版地址的 QQ 号码")
    val qq: Long by value()

    @ValueDescription("申请开源版地址的授权码")
    val authKey: String by value()

    @ValueDescription("是否显示绒狸开源版尾巴（推荐开启）")
    val showTail: Boolean by value(true)

    @ValueDescription("私聊消息响应列表，填写 QQ 号码")
    val responseFriends: List<Long> by value()

    @ValueDescription("群聊消息相应列表，填写 QQ 群号码")
    val responseGroups: List<Long> by value()
}