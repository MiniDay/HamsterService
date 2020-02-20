package cn.hamster3.service.bungee;

import cn.hamster3.service.bungee.command.ServiceCommand;
import cn.hamster3.service.bungee.listener.*;
import cn.hamster3.service.bungee.service.ServiceGroup;
import cn.hamster3.service.bungee.service.ServiceManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.logging.Logger;

public class BungeeService extends Plugin implements Listener {
    private static Logger logger;

    public static void main(String[] args) {
    }

    public static void log(String msg) {
        logger.info(msg);
    }

    public static void log(String msg, Object... objects) {
        logger.info(String.format(msg, objects));
    }

    public static void warning(String msg) {
        logger.warning(msg);
    }

    public static void warning(String msg, Object... objects) {
        logger.warning(String.format(msg, objects));
    }

    @Override
    public void onEnable() {
        logger = getLogger();
        PluginManager manager = ProxyServer.getInstance().getPluginManager();
        Configuration config = getConfig();
        Configuration groups = config.getSection("groups");
        if (config.getBoolean("debug.enable")) {
            if (config.getBoolean("debug.start"))
                manager.registerListener(this, new GroupStartListener());
            if (config.getBoolean("debug.close"))
                manager.registerListener(this, new GroupCloseListener());

            if (config.getBoolean("debug.register"))
                manager.registerListener(this, new ServiceRegisterListener());

            if (config.getBoolean("debug.send"))
                manager.registerListener(this, new GroupSendListener());
            if (config.getBoolean("debug.receive"))
                manager.registerListener(this, new GroupReceiveListener());

            if (config.getBoolean("debug.broadcast"))
                manager.registerListener(this, new GroupBroadcastListener());

            if (config.getBoolean("debug.connect"))
                manager.registerListener(this, new ServiceConnectListener());

        }
        for (String name : groups.getKeys()) {
            Configuration groupConfig = groups.getSection(name);
            String host = groupConfig.getString("host");
            int port = groupConfig.getInt("port");
            HashMap<String, String> serverID = new HashMap<>();
            Configuration serverIDConfig = groupConfig.getSection("serverID");
            for (String password : serverIDConfig.getKeys()) {
                serverID.put(password, serverIDConfig.getString(password));
            }
            new ServiceGroup(host, port, name, serverID).start();
        }
        manager.registerCommand(this, new ServiceCommand());
    }

    @Override
    public void onDisable() {
        for (ServiceGroup group : ServiceManager.getGroups()) {
            if (group.isClosed()) {
                continue;
            }
            try {
                group.close().await();
            } catch (InterruptedException ignored) {
            }
        }
    }

    private Configuration getConfig() {
        File file = new File(getDataFolder(), "service.yml");
        if (!file.exists()) {
            return defaultConfig();
        }
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "service.yml"));
        } catch (Exception e) {
            e.printStackTrace();
            warning("未找到配置文件, 准备创建默认配置文件...");
            return defaultConfig();
        }
    }

    private Configuration defaultConfig() {
        try {
            if (getDataFolder().mkdir()) {
                log("创建插件文件夹...");
            }
            File file = new File(getDataFolder(), "service.yml");
            InputStream in = getResourceAsStream("service.yml");
            Files.copy(in, file.toPath());
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "service.yml"));
        } catch (Exception ignored) {
        }
        return null;
    }
}
