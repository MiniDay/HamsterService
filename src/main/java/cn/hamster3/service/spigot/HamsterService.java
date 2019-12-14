package cn.hamster3.service.spigot;

import cn.hamster3.service.spigot.event.*;
import cn.hamster3.service.spigot.listener.ServiceConnectListener;
import cn.hamster3.service.spigot.listener.ServiceReceiveListener;
import cn.hamster3.service.spigot.listener.ServiceRegisterListener;
import cn.hamster3.service.spigot.listener.ServiceSendListener;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Logger;

public final class HamsterService extends JavaPlugin {
    static ArrayList<ServicePreSendEvent> messages;

    private static Logger logger;
    private static Channel channel;
    private static Bootstrap bootstrap;
    private static String serverName;
    private static String serviceHost;
    private static int servicePort;
    private static String servicePassword;
    private static NioEventLoopGroup loopGroup;

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

    public static void sendMessage(String message) {
        if (message == null) {
            throw new IllegalArgumentException("消息不能被设置为 null !");
        }
        if (message.getBytes(StandardCharsets.UTF_8).length >= 0xffff) {
            throw new IllegalArgumentException("消息总长度不能超过 65535 字节!");
        }
        ServicePreSendEvent event = new ServicePreSendEvent(message);
        Bukkit.getPluginManager().callEvent(event);
        sendMessage(event);
    }

    public static void sendMessage(String message, Object... objects) {
        sendMessage(String.format(message, objects));
    }

    public static void sendMessage(String tag, String message) {
        if (tag == null) {
            sendMessage(message);
            return;
        }
        if (!tag.matches("[a-zA-Z0-9_]*")) {
            throw new IllegalArgumentException("tag 只能使用字母、数字或下划线!");
        }
        if (message == null) {
            throw new IllegalArgumentException("消息不能被设置为 null !");
        }
        if ((tag + ":" + message).getBytes(StandardCharsets.UTF_8).length >= 0xffff) {
            throw new IllegalArgumentException("消息总长度不能超过 65535 字节!");
        }
        ServicePreSendEvent event = new ServicePreSendEvent(tag, message);
        Bukkit.getPluginManager().callEvent(event);
        sendMessage(event);
    }

    public static void sendMessage(String tag, String message, Object... objects) {
        sendMessage(tag, String.format(message, objects));
    }

    static void sendMessage(ServicePreSendEvent event) {
        if (event.isCancelled()) {
            return;
        }
        // 如果通道暂时不可用则先缓存这些消息
        if (channel == null || !channel.isActive()) {
            messages.add(event);
            return;
        }
        ChannelFutureListener listener = future -> {
            if (future.isSuccess()) {
                if (event.hasTag()) {
                    Bukkit.getPluginManager().callEvent(new ServiceSendEvent(event.getTag(), event.getMessage()));
                } else {
                    Bukkit.getPluginManager().callEvent(new ServiceSendEvent(event.getMessage()));
                }
            } else {
                if (event.hasTag()) {
                    Bukkit.getPluginManager().callEvent(new ServiceSendEvent(event.getTag(), event.getMessage(), future.cause()));
                } else {
                    Bukkit.getPluginManager().callEvent(new ServiceSendEvent(event.getMessage(), future.cause()));
                }
            }
        };
        if (!event.hasTag()) {
            channel.writeAndFlush(event.getMessage()).addListener(listener);
        } else {
            channel.writeAndFlush(event.getTag() + ":" + event.getMessage()).addListener(listener);
        }
        System.out.println(event.getTag());
        System.out.println(event.getMessage());
    }

    public static String getServerName() {
        return serverName;
    }

    public static String getServiceHost() {
        return serviceHost;
    }

    public static int getServicePort() {
        return servicePort;
    }

    static void reconnect() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {
        }
        if (loopGroup == null) {
            return;
        }
        if (channel != null && channel.isActive()) {
            return;
        }
        Bukkit.getPluginManager().callEvent(new ServiceReconnectEvent());
        channel = null;
        Bukkit.getPluginManager().callEvent(new ServiceConnectEvent(true));
        connect();
    }

    static void setName(String name) {
        serverName = name;
    }

    private static void connect() {
        bootstrap.connect(serviceHost, servicePort).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                Bukkit.getPluginManager().callEvent(new ServiceConnectedEvent());
                channel = future.channel();
                InetSocketAddress address = new InetSocketAddress(Bukkit.getIp(), Bukkit.getPort());
                Bukkit.getPluginManager().callEvent(new ServiceRegisterEvent());
                sendMessage("HamsterService", String.format("register %s %s %d", servicePassword, address.getHostString(), address.getPort()));
            } else {
                Bukkit.getPluginManager().callEvent(new ServiceConnectedEvent(future.cause()));
                reconnect();
            }
        });
    }

    @Override
    public void onEnable() {
        messages = new ArrayList<>();

        logger = getLogger();
        saveDefaultConfig();
        reloadConfig();
        FileConfiguration config = getConfig();
        PluginManager manager = Bukkit.getPluginManager();
        if (config.getBoolean("debug.enable")) {

            if (config.getBoolean("debug.send"))
                manager.registerEvents(new ServiceSendListener(), this);
            if (config.getBoolean("debug.receive"))
                manager.registerEvents(new ServiceReceiveListener(), this);

            if (config.getBoolean("debug.connect"))
                manager.registerEvents(new ServiceConnectListener(), this);

            if (config.getBoolean("debug.register"))
                manager.registerEvents(new ServiceRegisterListener(), this);
        }
        serviceHost = config.getString("host");
        servicePort = config.getInt("port");
        servicePassword = config.getString("password");
        loopGroup = new NioEventLoopGroup(3);
        bootstrap = new Bootstrap();
        bootstrap.group(loopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ServiceInitHandler());
        Bukkit.getPluginManager().callEvent(new ServiceConnectEvent(false));
        connect();
    }

    @Override
    public void onDisable() {
        try {
            loopGroup.shutdownGracefully().await();
            loopGroup = null;
        } catch (InterruptedException ignored) {
        }
    }
}
