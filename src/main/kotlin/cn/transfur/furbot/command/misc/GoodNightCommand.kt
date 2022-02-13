package cn.transfur.furbot.command.misc

import cn.transfur.furbot.Config
import cn.transfur.furbot.command.FurbotSimpleCommand
import net.mamoe.mirai.console.command.MemberCommandSenderOnMessage
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.isOperator
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.buildMessageChain
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object GoodNightCommand : FurbotSimpleCommand("晚安") {
    override val description: String = "Mute self if is normal member, or mute a certain member if is op"

    @Handler
    suspend fun MemberCommandSenderOnMessage.run() {
        if (user.isOperator()) {
            val message = buildMessageChain(2) {
                // Ping
                add(At(user))

                // Info
                add("不可以晚安哦")
            }

            group.sendMessage(message)
        } else {
            execute(user, user)
        }
    }

    @Handler
    suspend fun MemberCommandSenderOnMessage.run(ping: At) {
        if (!user.isOperator() || group[ping.target]!!.isOperator()) {
            val message = buildMessageChain(2) {
                // Ping
                add(At(user))

                // Info
                add("你不可以向 ${ping.getDisplay(group)} 说晚安哦")
            }

            group.sendMessage(message)
        } else {
            execute(user, group[ping.target]!!)
        }
    }

    private suspend fun execute(executor: Member, target: Member) {
        val startTime = LocalTime.parse(Config.goodNight.startTime)
        val endTime = LocalTime.parse(Config.goodNight.endTime)

        val currentDateTime = LocalDateTime.now()

        val atNight = currentDateTime.toLocalTime() >= startTime
        val beforeDawn = currentDateTime.toLocalTime() <= endTime

        if (atNight || beforeDawn) {
            // Message to sent
            val message = buildMessageChain(2) {
                // Ping
                add(At(target))

                // Info
                val daySpecification = if (atNight) "明天" else "今天"
                add("禁言到$daySpecification ${Config.goodNight.endTime}，晚安！")
            }

            target.group.sendMessage(message)

            // Mute duration to apply
            val endDate = currentDateTime.toLocalDate().let { if (atNight) it.plusDays(1) else it }
            val endDateTime = LocalDateTime.of(endDate, endTime)
            val muteDuration = currentDateTime.until(endDateTime, ChronoUnit.SECONDS)

            target.mute(muteDuration.toInt())
        } else {
            val message = buildMessageChain(2) {
                // Ping
                add(At(executor))

                // Info
                add("太早了，不能晚安")
            }

            executor.group.sendMessage(message)
        }
    }
}