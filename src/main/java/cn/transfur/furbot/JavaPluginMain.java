package cn.transfur.furbot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;

import cn.transfur.furbot.base.Config;
import cn.transfur.furbot.bot.BotParser;
import cn.transfur.furbot.bot.BotResponse;

/**
 * 可以使用 {@code src/test/kotlin/RunMirai.kt} 在 IDE 里直接调试，
 * 不用复制到 mirai-console-loader 或其他启动器中调试
 */

public final class JavaPluginMain extends JavaPlugin {
    public static final JavaPluginMain INSTANCE = new JavaPluginMain();
    private Gson gson = null;
    private Config config = null;

    private JavaPluginMain() {
        super(new JvmPluginDescriptionBuilder("cn.transfur.furbot", "0.1.1")
                .name("FurBot")
                .author("Jmeow")
                .info("furbot-mirai")
                .build());
    }

    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        gson = new GsonBuilder().disableHtmlEscaping().create();
        initProperties();
        if (config == null) {
            config = new Config();
            getLogger().info("模板配置文件已生成，请在/config/cn.transfur.furbot/config.yml中编辑配置文件。");
        }
        getLogger().info("Plugin init success. Bot id : " + config.getFurbot().getQq());
    }

    @Override
    public void onEnable() {
        EventChannel<Event> eventChannel = GlobalEventChannel.INSTANCE.parentScope(this);
        //监听群消息
        eventChannel.subscribeAlways(GroupMessageEvent.class, g -> {
            if (!config.getFurbot().isResponseGroup()) {
                return;
            }
            //解析并响应消息
            BotParser.parseMessage(g, new BotResponse() {
                @Override
                public void onResponse(MessageChain rspMessage) {
                    g.getGroup().sendMessage(rspMessage);
                }

                @Override
                public void onError(String msg) {
                    getLogger().info("Furbot error, msg: " + msg);
                }
            });
        });
        //监听好友消息
        eventChannel.subscribeAlways(FriendMessageEvent.class, f -> {
            if (!config.getFurbot().isResponseFriend()) {
                return;
            }
            //解析并响应消息
            BotParser.parseMessage(f, new BotResponse() {
                @Override
                public void onResponse(MessageChain rspMessage) {
                    f.getSender().sendMessage(rspMessage);
                }

                @Override
                public void onError(String msg) {
                    getLogger().info("Furbot error, msg: " + msg);
                }
            });
        });
    }

    /**
     * 初始化配置文件
     */
    private void initProperties() {
        Yaml yaml = new Yaml(new Constructor(Config.class));
        File file = resolveConfigFile("config.yml");
        try {
            config = yaml.load(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            getLogger().error("配置文件未找到");
            createNewConfigFile(file);
        }
    }

    /**
     * 创建新的配置文件
     *
     * @param file File
     */
    private void createNewConfigFile(File file) {
        try (InputStream in = getResourceAsStream("TemplateConfig.yml");
             FileOutputStream out = new FileOutputStream(file)) {
            file.createNewFile();
            streamCopy(in, out);
        } catch (IOException ioException) {
            getLogger().error("文件写入失败");
        }
    }

    private void streamCopy(InputStream in, FileOutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int byteRead = in.read(buffer);
            if (byteRead == -1)
                break;
            out.write(buffer, 0, byteRead);
        }
    }

    public Config getConfig() {
        return config;
    }
}
