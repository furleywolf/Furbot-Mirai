package cn.transfur.furbot.bot;

/**
 * Date: 2022/1/28
 * Author: Jmeow
 */
public interface BotResponse {

    void onResponse(String rspText);

    void onError();
}