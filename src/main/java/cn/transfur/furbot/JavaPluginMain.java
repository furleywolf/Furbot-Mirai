package cn.transfur.furbot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

import cn.transfur.furbot.bot.BotParser;
import cn.transfur.furbot.bot.BotResponse;


/**
 * 使用 Java 请把
 * {@code /src/main/resources/META-INF.services/net.mamoe.mirai.console.plugin.jvm.JvmPlugin}
 * 文件内容改成 {@code cn.transfur.furbot.JavaPluginMain} <br/>
 * 也就是当前主类全类名
 * <p>
 * 使用 Java 可以把 kotlin 源集删除且不会对项目有影响
 * <p>
 * 在 {@code settings.gradle.kts} 里改构建的插件名称、依赖库和插件版本
 * <p>
 * 在该示例下的 {@link JvmPluginDescription} 修改插件名称，id 和版本等
 * <p>
 * 可以使用 {@code src/test/kotlin/RunMirai.kt} 在 IDE 里直接调试，
 * 不用复制到 mirai-console-loader 或其他启动器中调试
 */

public final class JavaPluginMain extends JavaPlugin {
    public static final JavaPluginMain INSTANCE = new JavaPluginMain();
    private Gson gson = null;

    private JavaPluginMain() {
        super(new JvmPluginDescriptionBuilder("cn.transfur.furbot", "0.1.0")
                .info("furbot-mirai")
                .build());
    }

    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    @Override
    public void onEnable() {
        initProperties();
        getLogger().info("Plugin init success.");

        EventChannel<Event> eventChannel = GlobalEventChannel.INSTANCE.parentScope(this);
        eventChannel.subscribeAlways(GroupMessageEvent.class, g -> {
            //监听群消息

        });
        eventChannel.subscribeAlways(FriendMessageEvent.class, f -> {
            //监听好友消息
            String msg = getPlainText(f.getMessage());

            getLogger().info("Get Message " + msg);

            BotParser.parseOrRepeat(msg, new BotResponse() {
                @Override
                public void onResponse(String rspText) {

                    getLogger().info("RSP " + rspText);

//                    f.getSender().sendMessage(rspText);
                }

                @Override
                public void onError() {

                }
            });
        });
    }

    /**
     * 只截取第一段文本消息
     * @param message
     * @return
     */
    public String getPlainText(MessageChain message){
        for (SingleMessage singleMessage : message) {
            if (singleMessage instanceof PlainText) {
                return ((PlainText) singleMessage).getContent();
            }
        }
        return "";
    }

    /**
     * 初始化配置文件
     */
    private void initProperties() {
        InputStream in = JavaPluginMain.class.getClassLoader().getResourceAsStream("config.yml");
        Yaml yaml = new Yaml(new Constructor(Config.class));
        Config config = yaml.load(in);

        getLogger().info("prop1 = " + config.getFurbot().getProp1());
        getLogger().info("prop2 = " + config.getFurbot().getProp1());
    }

}
