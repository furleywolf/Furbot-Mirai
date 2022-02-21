package cn.transfur.furbot.command.furbot

import cn.transfur.furbot.command.FurbotSimpleCommand
import cn.transfur.furbot.command.ImageAwareCommand
import cn.transfur.furbot.data.FurPic
import cn.transfur.furbot.network.TailApiClient
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

abstract class GetFurCommand(primaryName: String) : FurbotSimpleCommand(primaryName), ImageAwareCommand {
    protected suspend fun getFurPicSimple(
        apiPath: String,
        vararg extraParameters: Pair<String, Any?>
    ): FurPic? = TailApiClient.getFromTailApi(FurPic.serializer(), apiPath, *extraParameters)

    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override val prefixOptional: Boolean = true
}