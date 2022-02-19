package cn.transfur.furbot

import cn.transfur.furbot.command.admin.FurbotControlCommand
import cn.transfur.furbot.command.furbot.GetFidsByNameCommand
import cn.transfur.furbot.command.furbot.GetFurByFidCommand
import cn.transfur.furbot.command.furbot.GetFurByNameCommand
import cn.transfur.furbot.command.furbot.GetFurRandCommand
import cn.transfur.furbot.command.misc.GoodNightCommand
import cn.transfur.furbot.command.misc.HelpCommand
import cn.transfur.furbot.network.TailApiClient
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit
import net.mamoe.mirai.console.plugin.PluginManager
import net.mamoe.mirai.console.plugin.id
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.events.MessageEvent

object KotlinPluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "cn.transfur.furbot",
        version = "0.1.2",
        name = "Furbot"
    ) {
        author("Jmeow, Peanuuutz")
        info("furbot-mirai")
    }
) {
    private var listener: Listener<MessageEvent>? = null

    val userCommands: Array<Command> by lazy {
        arrayOf(
            // furbot
            GetFurByFidCommand,
            GetFurByNameCommand,
            GetFurRandCommand,
            GetFidsByNameCommand,

            // misc
            GoodNightCommand,
            HelpCommand
        )
    }

    override fun onEnable() {
        Config.reload()

        TailApiClient.open()

        registerCommands()

        subscribe()
    }

    private fun registerCommands() {
        with(FurbotControlCommand) {
            register()

            // To enable initial operation: `.furbot on`
            AbstractPermitteeId.AnyMemberFromAnyGroup.permit(permission)
        }

        userCommands.forEach { it.register() }
    }

    @OptIn(ConsoleExperimentalApi::class, ExperimentalCommandDescriptors::class)
    private fun subscribe() {
        // Use chat-command when possible
        if (PluginManager.plugins.find { it.id == "net.mamoe.mirai.console.chat-command" } == null) {
            listener = GlobalEventChannel.subscribeAlways(
                coroutineContext = CoroutineExceptionHandler { _, throwable -> logger.error(throwable) },
                priority = EventPriority.MONITOR
            ) {
                launch {
                    val result = CommandManager.executeCommand(toCommandSender(), message)
                    throw result.exception ?: return@launch
                }
            }
        }
    }

    override fun onDisable() {
        TailApiClient.close()

        listener?.complete()
    }
}
