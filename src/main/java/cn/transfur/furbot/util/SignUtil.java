package cn.transfur.furbot.util;

import java.util.Locale;

/**
 * Date: 2022/1/28
 * Author: Jmeow
 */
public class SignUtil {

    /**
     * 签名构建
     *
     * @param path 路径，注意不以"/"开头
     * @param time UNIX时间戳
     * @param key  授权Key
     * @return MD5签名
     */
    public static String buildSignString(String path, String time, String key) {
        String str = path + "-" + time + "-" + key;
        return MD5Util.MD5EncodeUtf8(str).toLowerCase(Locale.ROOT);
    }

    /**
     * 获取当前UNIX时间戳
     *
     * @return 时间戳，单位秒
     */
    public static String getTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000L);
    }
}