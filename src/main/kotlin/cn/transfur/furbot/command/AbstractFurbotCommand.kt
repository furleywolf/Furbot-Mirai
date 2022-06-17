package cn.transfur.furbot.command

import cn.transfur.furbot.KotlinPluginMain
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.RawCommand

abstract class FurbotSimpleCommand(
    primaryName: String,
    description: String
) : SimpleCommand(
    owner = KotlinPluginMain,
    primaryName = primaryName,
    description = description
), FurbotCommand

abstract class FurbotCompositeCommand(
    primaryName: String,
    description: String
) : CompositeCommand(
    owner = KotlinPluginMain,
    primaryName = primaryName,
    description = description
), FurbotCommand

abstract class FurbotRawCommand(
    primaryName: String,
    description: String
) : RawCommand(
    owner = KotlinPluginMain,
    primaryName = primaryName,
    description = description
)