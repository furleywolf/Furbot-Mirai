package cn.transfur.furbot.command.admin

import cn.transfur.furbot.KotlinPluginMain
import cn.transfur.furbot.command.FurbotCompositeCommand
import cn.transfur.furbot.command.GroupOnlyCommand
import cn.transfur.furbot.util.sendMessage
import net.mamoe.mirai.console.command.Command.Companion.allNames
import net.mamoe.mirai.console.command.MemberCommandSenderOnMessage
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.PermissionService.Companion.cancel
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.message.data.At

object FurbotControlCommand : FurbotCompositeCommand(
    primaryName = "furbot",
    description = "Control all commands in the furbot scope"
), GroupOnlyCommand {
    @SubCommand("on", "开")
    suspend fun MemberCommandSenderOnMessage.on(commandName: String? = null) = run { group, sender ->
        // Warn if is not operator
        if (!sender.isOperator()) {
            group.sendMessage {
                // Ping
                add(At(sender))

                // Warn
                add("权限不足，只有本群管理员才能使用此命令")
            }
            return
        }

        if (commandName == null) {
            executeAll(group, true)
        } else {
            executeSingle(group, true, commandName)
        }
    }

    @SubCommand("off", "关")
    suspend fun MemberCommandSenderOnMessage.off(commandName: String? = null) = run { group, sender ->
        // Warn if is not operator
        if (!sender.isOperator()) {
            group.sendMessage {
                // Ping
                add(At(sender))

                // Warn
                add("权限不足，只有本群管理员才能使用此命令")
            }
            return
        }

        if (commandName == null) {
            executeAll(group, false)
        } else {
            executeSingle(group, false, commandName)
        }
    }

    private suspend fun executeAll(group: Group, operation: Boolean) {
        val permitteeId = AbstractPermitteeId.AnyMember(group.id)
        val operationSpecification = if (operation) "开启" else "关闭"

        if (operation) {
            for (command in KotlinPluginMain.userCommands) {
                permitteeId.permit(command.permission)
            }
        } else {
            for (command in KotlinPluginMain.userCommands) {
                permitteeId.cancel(command.permission, false)
            }
        }

        group.sendMessage("已${operationSpecification}所有命令")
    }

    private suspend fun executeSingle(group: Group, operation: Boolean, commandName: String) {
        val permitteeId = AbstractPermitteeId.AnyMember(group.id)
        val operationSpecification = if (operation) "开启" else "关闭"

        val command = KotlinPluginMain.userCommands.find { commandName in it.allNames }

        if (command == null) {
            group.sendMessage("找不到命令：$commandName")
            return
        }

        if (operation) {
            permitteeId.permit(command.permission)
        } else {
            permitteeId.cancel(command.permission, false)
        }

        group.sendMessage("已${operationSpecification}命令：$commandName")
    }
}
