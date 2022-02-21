package cn.transfur.furbot.command

import cn.transfur.furbot.Config
import cn.transfur.furbot.KotlinPluginMain
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.FriendCommandSenderOnMessage
import net.mamoe.mirai.console.command.MemberCommandSenderOnMessage
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.User

abstract class FurbotSimpleCommand(
    primaryName: String
) : SimpleCommand(KotlinPluginMain, primaryName), FurbotCommand {
    protected inline fun CommandSenderOnMessage<*>.runBoth(action: (target: Contact, sender: User) -> Unit) {
        when (this) {
            is MemberCommandSenderOnMessage -> run(action)
            is FriendCommandSenderOnMessage -> run(action)
        }
    }

    protected inline fun MemberCommandSenderOnMessage.run(action: (group: Group, sender: Member) -> Unit) {
        if (Config.furbot.respondGroups)
            action(group, user)
    }

    protected inline fun FriendCommandSenderOnMessage.run(action: (friend: Friend, sender: Friend) -> Unit) {
        if (Config.furbot.respondFriends)
            action(user, user)
    }
}

abstract class FurbotCompositeCommand(
    primaryName: String
) : CompositeCommand(KotlinPluginMain, primaryName), FurbotCommand {
    protected inline fun CommandSenderOnMessage<*>.runBoth(action: (Contact, User) -> Unit) {
        when (this) {
            is MemberCommandSenderOnMessage -> run(action)
            is FriendCommandSenderOnMessage -> run(action)
        }
    }

    protected inline fun MemberCommandSenderOnMessage.run(action: (Group, Member) -> Unit) {
        if (Config.furbot.respondGroups)
            action(group, user)
    }

    protected inline fun FriendCommandSenderOnMessage.run(action: (Friend, Friend) -> Unit) {
        if (Config.furbot.respondFriends)
            action(user, user)
    }
}