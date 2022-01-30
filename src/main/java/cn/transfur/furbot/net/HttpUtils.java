package cn.transfur.furbot.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {

    private static final OkHttpClient client;

    static {
        client = new OkHttpClient();
    }

    /**
     * get
     */
    public static Response doGet(String host, String path, Map<String, String> headers,
                                 Map<String, String> querys) {

        Headers.Builder header = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                header.add(e.getKey(), e.getValue());
            }
        }

        try {
            Request request = new Request.Builder()
                    .url(buildUrl(host, path, querys))
                    .headers(header.build())
                    .build();

            return client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] doGetFileInputStream(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            return client.newCall(request).execute().body().bytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String buildUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!isBlank(path)) {
            sbUrl.append(path);
        }
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (isBlank(query.getKey()) && !isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!isBlank(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }

        return sbUrl.toString();
    }

    private static boolean isBlank(String str) {
        return str == null || "".equals(str);
    }
}