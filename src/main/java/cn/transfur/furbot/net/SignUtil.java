package cn.transfur.furbot.net;

import java.util.Locale;

import cn.transfur.furbot.JavaPluginMain;

/**
 * Date: 2022/1/28
 * Author: Jmeow
 */
public class SignUtil {

    public static String buildSignString(String path, String time, String key) {
        String str = path + "-" + time + "-" + key;
        JavaPluginMain.INSTANCE.getLogger().info("RawStr " + str);
        return MD5Util.MD5EncodeUtf8(str).toLowerCase(Locale.ROOT);
    }

}