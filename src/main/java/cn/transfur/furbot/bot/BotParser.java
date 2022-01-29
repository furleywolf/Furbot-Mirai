package cn.transfur.furbot.bot;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import net.mamoe.mirai.utils.ExternalResource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.transfur.furbot.base.Config;
import cn.transfur.furbot.JavaPluginMain;
import cn.transfur.furbot.model.FurryPic;
import cn.transfur.furbot.net.FurbotApi;
import cn.transfur.furbot.net.HttpUtils;

/**
 * Bot解析器，对特定文本的响应与应答
 * Date: 2022/1/28
 * Author: Jmeow
 */

public class BotParser {

    private static final String T_RAND_FURSUIT = "来只毛";
    private static final String T_GET_FURSUIT_BY_NAME = "来只\\s*(.*)";
    private static final String T_GET_FURSUIT_BY_ID = "找毛图\\s*(.*)";

    private static final Config config = JavaPluginMain.INSTANCE.getConfig();

    /**
     * 响应各种消息类型
     *
     * @param message  消息体
     * @param response 状态或内容返回
     */
    public static void parseMessage(MessageEvent message, BotResponse response) {
        String msg = BotParser.getFirstPlainText(message.getMessage());

        Contact sender = null;
        if (message instanceof GroupMessageEvent) {
            sender = ((GroupMessageEvent) message).getGroup();
        } else {
            sender = message.getSender();
        }

        if (msg == null) {
            return;
        }

        //来只毛
        if (msg.equals(T_RAND_FURSUIT)) {
            getFursuitRand(sender, response);
            return;
        }

        //来只 <NAME>
        String fursuitByName = easyRegexp(msg, T_GET_FURSUIT_BY_NAME);
        if (fursuitByName != null) {
            getFursuitByName(sender, fursuitByName, response);
            return;
        }

        //找毛图 <ID>
        String fursuitById = easyRegexp(msg, T_GET_FURSUIT_BY_ID);
        if (fursuitById != null) {
            getFursuitById(sender, fursuitById, response);
            return;
        }
    }

    /**
     * 随机获取一只毛毛
     *
     * @param response 响应回调
     */
    public static void getFursuitRand(Contact sender, BotResponse response) {

        FurryPic furryPic = FurbotApi.getFursuitRand(
                config.getFurbot().getQq(), config.getFurbot().getAuthKey());

        if (furryPic == null) {
            response.onError("API接口获取失败");
            return;
        }

        byte[] bytes = HttpUtils.doGetFileInputStream(furryPic.getUrl());
        if (bytes == null) {
            response.onError("OSS图片拉取失败");
            return;
        }

        furryPic.setImageBytes(bytes);

        MessageChainBuilder builder = new MessageChainBuilder()
                .append("---每日吸毛Bot---\n" +
                        "今天你吸毛了嘛？\n" +
                        "FurID:" + furryPic.getId() + "\n" +
                        "毛毛名字:" + furryPic.getName() + " \n" +
                        "搜索方法:全局随机")
                .append(sender.uploadImage(ExternalResource.create(bytes)));

        MessageChain build = builder.build();

        response.onResponse(build);
    }


    /**
     * 根据名称查找毛毛
     *
     * @param sender   发送者
     * @param name     名称
     * @param response 响应回调
     */
    public static void getFursuitByName(Contact sender, String name, BotResponse response) {

        FurryPic furryPic = FurbotApi.getFursuitByName(
                config.getFurbot().getQq(), config.getFurbot().getAuthKey(), name);

        if (furryPic == null) {
            response.onError("API接口获取失败");
            return;
        }

        if (furryPic.getName() == null) {
            MessageChainBuilder builder = new MessageChainBuilder()
                    .append("这只毛毛还没有被收录，请联系开发者添加哦");

            response.onResponse(builder.build());
            return;
        }

        byte[] bytes = HttpUtils.doGetFileInputStream(furryPic.getUrl());
        if (bytes == null) {
            response.onError("OSS图片拉取失败");
            return;
        }

        furryPic.setImageBytes(bytes);

        MessageChainBuilder builder = new MessageChainBuilder()
                .append("---每日吸毛Bot---\n" +
                        "FurID:" + furryPic.getId() + "\n" +
                        "毛毛名字:" + furryPic.getName() + " \n" +
                        "搜索方法: 模糊")
                .append(sender.uploadImage(ExternalResource.create(bytes)));

        MessageChain build = builder.build();

        response.onResponse(build);
    }

    /**
     * 根据FID查找毛毛
     *
     * @param sender   发送者
     * @param fid      id编号
     * @param response 响应回调
     */
    public static void getFursuitById(Contact sender, String fid, BotResponse response) {

        FurryPic furryPic = FurbotApi.getFursuitByID(
                config.getFurbot().getQq(), config.getFurbot().getAuthKey(), fid);

        if (furryPic == null) {
            response.onError("API接口获取失败");
            return;
        }

        if (furryPic.getName() == null) {
            MessageChainBuilder builder = new MessageChainBuilder()
                    .append("这只毛毛还没有被收录，请联系开发者添加哦");

            response.onResponse(builder.build());
            return;
        }

        byte[] bytes = HttpUtils.doGetFileInputStream(furryPic.getUrl());
        if (bytes == null) {
            response.onError("OSS图片拉取失败");
            return;
        }

        furryPic.setImageBytes(bytes);

        MessageChainBuilder builder = new MessageChainBuilder()
                .append("---每日吸毛Bot---\n" +
                        "FurID:" + furryPic.getId() + "\n" +
                        "毛毛名字:" + furryPic.getName() + " \n" +
                        "搜索方法: 按FurID查找")
                .append(sender.uploadImage(ExternalResource.create(bytes)));

        MessageChain build = builder.build();

        response.onResponse(build);
    }

    /**
     * 只截取第一段文本消息
     *
     * @param message 消息
     * @return 截取后的文本
     */
    public static String getFirstPlainText(MessageChain message) {
        for (SingleMessage singleMessage : message) {
            if (singleMessage instanceof PlainText) {
                return ((PlainText) singleMessage).getContent();
            }
        }
        return null;
    }

    /**
     * 简单正则匹配，获取内容
     *
     * @param msg          消息Str
     * @param patternMatch 正则
     * @return 匹配到的内容
     */
    private static String easyRegexp(String msg, String patternMatch) {
        Pattern pattern = Pattern.compile(patternMatch);
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}