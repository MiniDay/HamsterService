package cn.hamster3.service.bungee.service;

import cn.hamster3.service.bungee.BungeeService;
import cn.hamster3.service.bungee.event.ServiceClientDisconnectedEvent;
import cn.hamster3.service.bungee.event.ServicePreSendClientEvent;
import cn.hamster3.service.bungee.event.ServicePostSendClientEvent;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.md_5.bungee.api.ProxyServer;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class ServiceConnection {
    private String bukkitAddress;
    private int bukkitPort;
    private String name;
    private boolean registered;

    private final ServiceGroup group;
    private final NioSocketChannel channel;

    public ServiceConnection(ServiceGroup group, NioSocketChannel channel) {
        this.group = group;
        this.channel = channel;
        registered = false;
    }

    /**
     * 向该服务器发送一条消息
     *
     * @param tag     消息标签
     * @param message 消息内容
     */
    public void sendMessage(String tag, String message) {
        if (tag == null) {
            sendMessage(message);
            return;
        }
        if (!tag.matches("[a-zA-Z0-9_]*")) {
            throw new IllegalArgumentException("tag 只能使用字母、数字或下划线!");
        }
        if ((tag + ":" + message).getBytes(StandardCharsets.UTF_8).length >= 0xffff) {
            throw new IllegalArgumentException("消息总长度不能超过 65535 字节!");
        }
        ServicePreSendClientEvent event = new ServicePreSendClientEvent(tag, message, group, this);
        ProxyServer.getInstance().getPluginManager().callEvent(event);
        executeSendEvent(event);
    }

    /**
     * 向该服务器发送一条消息
     *
     * @param message 消息内容
     */
    public void sendMessage(String message) {
        if (message == null) {
            throw new IllegalArgumentException("消息不能被设置为 null !");
        }
        if (message.getBytes(StandardCharsets.UTF_8).length >= 0xffff) {
            throw new IllegalArgumentException("消息长度不能超过 65535 字节!");
        }
        ServicePreSendClientEvent event = new ServicePreSendClientEvent(message, group, this);
        ProxyServer.getInstance().getPluginManager().callEvent(event);
        executeSendEvent(event);
    }

    private void executeSendEvent(ServicePreSendClientEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.hasTag()) {
            channel.writeAndFlush(event.getTag() + ":" + event.getMessage()).addListener(future -> {
                        if (future.isSuccess()) {
                            ProxyServer.getInstance().getPluginManager().callEvent(
                                    new ServicePostSendClientEvent(event.getTag(), event.getMessage(), group, this));
                        } else {
                            ProxyServer.getInstance().getPluginManager().callEvent(
                                    new ServicePostSendClientEvent(event.getTag(), event.getMessage(), group, this, future.cause())
                            );
                        }
                    }
            );
        } else {
            channel.writeAndFlush(event.getMessage()).addListener(future -> {
                        if (future.isSuccess()) {
                            ProxyServer.getInstance().getPluginManager().callEvent(
                                    new ServicePostSendClientEvent(event.getMessage(), group, this));
                        } else {
                            ProxyServer.getInstance().getPluginManager().callEvent(
                                    new ServicePostSendClientEvent(event.getMessage(), group, this, future.cause())
                            );
                        }
                    }
            );
        }
    }

    /**
     * 主动断开与该服务器的连接
     */
    public void disconnect() {
        channel.close();
        group.removeConnection(this);
        BungeeService.warning("桥接组 %s 主动断开了与 %s 的连接!", group.getName(), getName());
        ProxyServer.getInstance().getPluginManager().callEvent(new ServiceClientDisconnectedEvent(group, this));
    }

    public String getName() {
        return name == null ? channel.remoteAddress().toString() : name;
    }

    void setName(String name) {
        this.name = name;
    }

    public boolean isRegistered() {
        return registered;
    }

    void setRegistered() {
        this.registered = true;
    }

    public String getBukkitAddress() {
        return bukkitAddress;
    }

    void setBukkitAddress(String bukkitAddress) {
        this.bukkitAddress = bukkitAddress;
    }

    public int getBukkitPort() {
        return bukkitPort;
    }

    void setBukkitPort(int bukkitPort) {
        this.bukkitPort = bukkitPort;
    }

    public InetSocketAddress getConnectionAddress() {
        return channel.remoteAddress();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceConnection)) return false;

        ServiceConnection that = (ServiceConnection) o;

        return channel.equals(that.channel);
    }

    @Override
    public int hashCode() {
        return channel.hashCode();
    }
}
