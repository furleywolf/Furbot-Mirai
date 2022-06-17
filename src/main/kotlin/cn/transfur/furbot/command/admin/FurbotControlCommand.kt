package cn.transfur.furbot.command.admin

import cn.transfur.furbot.KotlinPluginMain
import cn.transfur.furbot.command.FurbotCompositeCommand
import cn.transfur.furbot.util.sendMessage
import net.mamoe.mirai.console.command.Command.Companion.allNames
import net.mamoe.mirai.console.command.MemberCommandSenderOnMessage
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.Permission
import net.mamoe.mirai.console.permission.PermissionService.Companion.cancel
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit
import net.mamoe.mirai.console.permission.PermitteeId
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.message.data.At

object FurbotControlCommand : FurbotCompositeCommand(
    primaryName = "furbot",
    description = "Control all commands in the furbot scope"
) {
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
            return@run
        }

        if (commandName == null) {
            executeAll(group, Operation.ON)
        } else {
            executeSingle(group, Operation.ON, commandName)
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
            return@run
        }

        if (commandName == null) {
            executeAll(group, Operation.OFF)
        } else {
            executeSingle(group, Operation.OFF, commandName)
        }
    }

    private suspend fun executeAll(group: Group, operation: Operation) {
        val permitteeId = AbstractPermitteeId.AnyMember(group.id)

        for (command in KotlinPluginMain.userCommands)
            operation.action(permitteeId, command.permission)
        group.sendMessage("已${operation.specification}所有命令")
    }

    private suspend fun executeSingle(group: Group, operation: Operation, commandName: String) {
        val permitteeId = AbstractPermitteeId.AnyMember(group.id)

        val command = KotlinPluginMain.userCommands.find { commandName in it.allNames }
        if (command == null) {
            group.sendMessage("找不到命令：$commandName")
            return
        }

        operation.action(permitteeId, command.permission)
        group.sendMessage("已${operation.specification}命令：$commandName")
    }

    private enum class Operation(val specification: String) {
        ON("开启") {
            override fun action(permitteeId: PermitteeId, permission: Permission) {
                permitteeId.permit(permission)
            }
        },
        OFF("关闭") {
            override fun action(permitteeId: PermitteeId, permission: Permission) {
                permitteeId.cancel(permission, false)
            }
        };

        abstract fun action(permitteeId: PermitteeId, permission: Permission)
    }
}
