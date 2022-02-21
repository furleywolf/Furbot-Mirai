package cn.transfur.furbot.command.furbot

import cn.transfur.furbot.command.FurbotSimpleCommand
import cn.transfur.furbot.command.ImageAwareCommand
import cn.transfur.furbot.data.FurPic
import cn.transfur.furbot.network.TailApiClient
import kotlinx.serialization.KSerializer
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

abstract class TailApiAwareCommand(
    private val apiPath: String,
    primaryName: String,
    description: String
) : FurbotSimpleCommand(primaryName, description) {
    protected suspend fun <T> getFromTailApi(
        dataSerializer: KSerializer<T>,
        vararg extraParameters: Pair<String, Any?>
    ): T? = TailApiClient.getFromTailApi(dataSerializer, apiPath, *extraParameters)
}

abstract class GetFurCommand(
    apiPath: String,
    primaryName: String,
    description: String
) : TailApiAwareCommand(apiPath, primaryName, description), ImageAwareCommand {
    protected suspend fun getFurPic(vararg extraParameters: Pair<String, Any?>): FurPic? {
        return getFromTailApi(FurPic.serializer(), *extraParameters)
    }

    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    override val prefixOptional: Boolean = true
}