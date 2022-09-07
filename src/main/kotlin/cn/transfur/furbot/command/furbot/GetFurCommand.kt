package cn.transfur.furbot.command.furbot

import cn.transfur.furbot.command.FurbotSimpleCommand
import cn.transfur.furbot.command.ImageAwareCommand
import cn.transfur.furbot.command.TailApiAwareCommand
import cn.transfur.furbot.data.FurPic
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

abstract class GetFurCommand(
    override val apiPath: String,
    primaryName: String,
    description: String
) : FurbotSimpleCommand(primaryName, description), TailApiAwareCommand, ImageAwareCommand {
    protected suspend fun getFurPic(vararg extraParameters: Pair<String, Any?>): FurPic? {
        return getFromTailApi(FurPic.serializer(), *extraParameters)
    }

    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override val prefixOptional: Boolean = true
}