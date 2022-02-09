import cn.transfur.furbot.KotlinPluginMain
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.utils.BotConfiguration
import java.nio.file.Paths

@OptIn(ConsoleExperimentalApi::class)
suspend fun main() {
    val runPath = Paths.get(System.getProperty("user.dir", ".")).resolve("run").toAbsolutePath()
    MiraiConsoleTerminalLoader.startAsDaemon(MiraiConsoleImplementationTerminal(runPath))

    val plugin = KotlinPluginMain

    plugin.load()
    plugin.enable()

    val id = System.getenv("id").toLong() // 在运行配置（Edit Configurations...）中设置环境变量
    val password = System.getenv("password")
    MiraiConsole.addBot(id, password) {
        fileBasedDeviceInfo()
        protocol = BotConfiguration.MiraiProtocol.ANDROID_WATCH
    }.alsoLogin()

    MiraiConsole.job.join()
}