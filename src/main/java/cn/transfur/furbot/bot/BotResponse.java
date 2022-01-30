package cn.transfur.furbot.bot;

import net.mamoe.mirai.message.data.MessageChain;

/**
 * Date: 2022/1/28
 * Author: Jmeow
 */
public interface BotResponse {

    void onResponse(MessageChain rspMessage);

    void onError(String msg);
}