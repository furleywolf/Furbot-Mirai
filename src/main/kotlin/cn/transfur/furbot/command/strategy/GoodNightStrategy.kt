package cn.transfur.furbot.command.strategy

import cn.transfur.furbot.Config
import cn.transfur.furbot.KotlinPluginMain
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.MemberPermission
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.buildMessageChain
import net.mamoe.mirai.utils.error
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object GoodNightStrategy : Strategy {
    override suspend fun respond(target: User) {
        if (target !is Member) return

        val startTime = LocalTime.parse(Config.goodNight.startTime)
        val endTime = LocalTime.parse(Config.goodNight.endTime)

        val currentDateTime = LocalDateTime.now()

        val atNight = currentDateTime.toLocalTime() >= startTime
        val beforeDawn = currentDateTime.toLocalTime() <= endTime

        if (atNight || beforeDawn) {
            if (target.group.botPermission < MemberPermission.ADMINISTRATOR) {
                KotlinPluginMain.logger.error { "Bot ${target.bot.id} is not op in group ${target.group.id}" }
                return
            }

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
                add(At(target))

                // Info
                add("太早了，不能晚安")
            }

            target.group.sendMessage(message)
        }
    }
}