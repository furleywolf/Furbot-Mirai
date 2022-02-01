package org.example.mirai.plugin

import cn.transfur.furbot.JavaPluginMain
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

@OptIn(ConsoleExperimentalApi::class)
suspend fun main() {
    MiraiConsoleTerminalLoader.startAsDaemon()

    JavaPluginMain.INSTANCE.load()
    JavaPluginMain.INSTANCE.enable()

    MiraiConsole.addBot(123456, "") {
        fileBasedDeviceInfo()
    }.alsoLogin()

    MiraiConsole.job.join()
}