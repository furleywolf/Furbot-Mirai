package cn.transfur.furbot.util;

import java.security.MessageDigest;

public class MD5Util {

    public static final String SALT = "";

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (byte value : b)
            resultSb.append(byteToHexString(value));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * @param origin 原始字符串
     * @param charsetName 字符集
     * @return 大写MD5
     */
    private static String MD5Encode(String origin, String charsetName) {
        String resultString = origin;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetName == null || "".equals(charsetName))
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
        } catch (Exception ignored) {
        }
        return resultString.toUpperCase();
    }

    public static String MD5EncodeUtf8(String origin) {
        origin = origin + SALT;
        return MD5Encode(origin, "utf-8");
    }

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

}