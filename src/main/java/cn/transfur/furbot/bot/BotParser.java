package cn.transfur.furbot.bot;

import java.util.HashMap;
import java.util.Map;

import cn.transfur.furbot.net.HttpUtils;
import cn.transfur.furbot.net.SignUtil;
import okhttp3.Response;

/**
 * Date: 2022/1/28
 * Author: Jmeow
 */
public class BotParser {

    public static void parseOrRepeat(String text, BotResponse response){
        if (text != null && text.equals("来只毛")){

            String timestamp = Long.toString(System.currentTimeMillis() / 1000L);

            Map<String, String> map = new HashMap<>();
            map.put("qq", "");
            map.put("timestamp", timestamp);
            map.put("sign", SignUtil.buildSignString("/api/v2/getFursuitRand", timestamp, ""));

            Response response1 = HttpUtils.doGet("https://api.tail.icu", "/api/v2/getFursuitRand", null, map);
            if (response1 != null){
                response.onResponse(response1.toString());
            }
        }
    }

}