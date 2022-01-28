package cn.transfur.furbot.net;

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
     *
     * @param host
     * @param path
     * @param headers
     * @param querys
     * @return
     * @throws Exception
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

//    /**
//     * post form
//     *
//     * @param host
//     * @param path
//     * @param method
//     * @param headers
//     * @param querys
//     * @param bodys
//     * @return
//     * @throws Exception
//     */
//    public static HttpResponse doPost(String host, String path, String method,
//                                      Map<String, String> headers,
//                                      Map<String, String> querys,
//                                      Map<String, String> bodys)
//            throws Exception {
//        HttpClient httpClient = wrapClient(host);
//
//        HttpPost request = new HttpPost(buildUrl(host, path, querys));
//        for (Map.Entry<String, String> e : headers.entrySet()) {
//            request.addHeader(e.getKey(), e.getValue());
//        }
//
//        if (bodys != null) {
//            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
//
//            for (String key : bodys.keySet()) {
//                nameValuePairList.add(new BasicNameValuePair(key, bodys.get(key)));
//            }
//            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
//            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
//            request.setEntity(formEntity);
//        }
//
//        return httpClient.execute(request);
//    }
//
//    /**
//     * Post String
//     *
//     * @param host
//     * @param path
//     * @param method
//     * @param headers
//     * @param querys
//     * @param body
//     * @return
//     * @throws Exception
//     */
//    public static HttpResponse doPost(String host, String path, String method,
//                                      Map<String, String> headers,
//                                      Map<String, String> querys,
//                                      String body)
//            throws Exception {
//        HttpClient httpClient = wrapClient(host);
//
//        HttpPost request = new HttpPost(buildUrl(host, path, querys));
//        for (Map.Entry<String, String> e : headers.entrySet()) {
//            request.addHeader(e.getKey(), e.getValue());
//        }
//
//        if (isNotBlank(body)) {
//            request.setEntity(new StringEntity(body, "utf-8"));
//        }
//
//        return httpClient.execute(request);
//    }
//
//    /**
//     * Post stream
//     *
//     * @param host
//     * @param path
//     * @param method
//     * @param headers
//     * @param querys
//     * @param body
//     * @return
//     * @throws Exception
//     */
//    public static HttpResponse doPost(String host, String path, String method,
//                                      Map<String, String> headers,
//                                      Map<String, String> querys,
//                                      byte[] body)
//            throws Exception {
//        HttpClient httpClient = wrapClient(host);
//
//        HttpPost request = new HttpPost(buildUrl(host, path, querys));
//        for (Map.Entry<String, String> e : headers.entrySet()) {
//            request.addHeader(e.getKey(), e.getValue());
//        }
//
//        if (body != null) {
//            request.setEntity(new ByteArrayEntity(body));
//        }
//
//        return httpClient.execute(request);
//    }

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