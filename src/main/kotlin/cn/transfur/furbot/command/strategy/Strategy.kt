package cn.transfur.furbot.command.strategy

import net.mamoe.mirai.contact.User

interface Strategy {
    suspend fun respond(target: User)
}