package cn.transfur.furbot.net;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.transfur.furbot.base.Common;
import cn.transfur.furbot.model.FurryPic;
import cn.transfur.furbot.model.ServerResponse;
import cn.transfur.furbot.util.SignUtil;
import okhttp3.Response;

/**
 * 网络请求调用
 * Date: 2022/1/29
 * Author: Jmeow
 */
public class FurbotApi {

    /**
     * 随机一只Fursuit的图片信息
     *
     * @param qq  botQQ号
     * @param key 鉴权Key
     * @return FursuitPic
     */
    public static FurryPic getFursuitRand(String qq, String key) {
        return getFursuitBasic(qq, key, Common.API_GET_FURSUIT_RAND, null);
    }

    /**
     * 根据ID查询一只Fursuit的图片信息
     *
     * @param qq  botQQ号
     * @param key 鉴权Key
     * @param fid FID
     * @return FursuitPic
     */
    public static FurryPic getFursuitByID(String qq, String key, String fid) {
        Map<String, String> map = new HashMap<>();
        map.put("fid", fid);
        return getFursuitBasic(qq, key, Common.API_GET_FURSUIT_BY_ID, map);
    }

    /**
     * 根据名称查询一只Fursuit的图片信息
     *
     * @param qq   botQQ号
     * @param key  鉴权Key
     * @param name 名称
     * @return FursuitPic
     */
    public static FurryPic getFursuitByName(String qq, String key, String name) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        return getFursuitBasic(qq, key, Common.API_GET_FURSUIT_BY_NAME, map);
    }

    /**
     * 通用获取图片信息
     *
     * @param qq  botQQ号
     * @param key 鉴权Key
     * @param api 接口
     * @return FursuitPic
     */
    private static FurryPic getFursuitBasic(String qq, String key, String api, Map<String, String> extraQuery) {
        String timestamp = SignUtil.getTimestamp();
        Map<String, String> query = new HashMap<>();
        query.put("qq", qq);
        query.put("timestamp", timestamp);
        query.put("sign", SignUtil.buildSignString(api, timestamp, key));

        if (extraQuery != null) {
            query.putAll(extraQuery);
        }

        Response rspRaw = HttpUtils.doGet(
                Common.API_HOST, api, null, query);

        try {
            if (rspRaw != null && rspRaw.body() != null) {
                String json = rspRaw.body().string();
                ServerResponse<FurryPic> rspData = new Gson().fromJson(json,
                        new TypeToken<ServerResponse<FurryPic>>() {
                        }.getType());
                return rspData.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}