package cn.hamster3.service.bungee.service;

import cn.hamster3.service.bungee.event.ServiceGroupPostBroadcastEvent;
import cn.hamster3.service.bungee.event.ServiceGroupCloseEvent;
import cn.hamster3.service.bungee.event.ServiceGroupPreBroadcastEvent;
import cn.hamster3.service.bungee.event.ServiceGroupStartEvent;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import net.md_5.bungee.api.ProxyServer;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unused")
public class ServiceGroup {
    private boolean closed;

    private final String host;
    private final int port;
    private final String name;
    private final HashMap<String, String> serverID;


    private final ServerBootstrap bootstrap;
    private final NioEventLoopGroup loopGroup;
    private final HashSet<ServiceConnection> connections;

    public ServiceGroup(String name, String host, int port, HashMap<String, String> serverID) {
        this.host = host;
        this.port = port;
        this.name = name;
        this.serverID = serverID;
        connections = new HashSet<>();

        bootstrap = new ServerBootstrap();
        loopGroup = new NioEventLoopGroup();
        bootstrap
                .group(loopGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ServiceInitHandler(this));
    }

    public void broadcast(String message) {
        if (message == null) {
            throw new IllegalArgumentException("消息不能被设置为 null !");
        }
        if (message.getBytes(StandardCharsets.UTF_8).length >= 0xffff) {
            throw new IllegalArgumentException("消息总长度不能超过 65535 字节!");
        }
        ServiceGroupPreBroadcastEvent event = new ServiceGroupPreBroadcastEvent(message, this);
        ProxyServer.getInstance().getPluginManager().callEvent(event);
        broadcast(event);
    }

    public void broadcast(String message, Object... objects) {
        broadcast(String.format(message, objects));
    }

    public void broadcast(String tag, String message) {
        if (tag == null) {
            broadcast(message);
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
        ServiceGroupPreBroadcastEvent event = new ServiceGroupPreBroadcastEvent(tag, message, this);
        ProxyServer.getInstance().getPluginManager().callEvent(event);
        broadcast(event);
    }

    public void broadcast(String tag, String message, Object... objects) {
        broadcast(tag, String.format(message, objects));
    }

    private void broadcast(ServiceGroupPreBroadcastEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.hasTag()) {
            for (ServiceConnection connection : connections) {
                connection.sendMessage(event.getTag(), event.getMessage());
            }
            ProxyServer.getInstance().getPluginManager().callEvent(new ServiceGroupPostBroadcastEvent(event.getTag(), event.getMessage(), this));
        } else {
            for (ServiceConnection connection : connections) {
                connection.sendMessage(event.getMessage());
            }
            ProxyServer.getInstance().getPluginManager().callEvent(new ServiceGroupPostBroadcastEvent(event.getMessage(), this));
        }
    }

    public ChannelFuture start() {
        ChannelFuture channelFuture = bootstrap.bind(host, port);
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                ProxyServer.getInstance().getPluginManager().callEvent(new ServiceGroupStartEvent(this, true));
            } else {
                ProxyServer.getInstance().getPluginManager().callEvent(new ServiceGroupStartEvent(this, false, future.cause()));
            }
        });
        return channelFuture;
    }

    public Future<?> close() {
        if (closed) {
            return null;
        }
        closed = true;
        Future<?> closeFuture = loopGroup.shutdownGracefully();
        closeFuture.addListener(future -> {
            if (future.isSuccess()) {
                ProxyServer.getInstance().getPluginManager().callEvent(new ServiceGroupCloseEvent(this, true));
            } else {
                ProxyServer.getInstance().getPluginManager().callEvent(new ServiceGroupCloseEvent(this, true, future.cause()));
            }
        });
        return closeFuture;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public String getServerID(String password) {
        return serverID.get(password);
    }

    public boolean isClosed() {
        return closed;
    }

    public HashSet<ServiceConnection> getConnections() {
        return new HashSet<>(connections);
    }

    void addConnection(ServiceConnection connection) {
        connections.add(connection);
    }

    void removeConnection(ServiceConnection connection) {
        connections.remove(connection);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceGroup)) return false;

        ServiceGroup group = (ServiceGroup) o;

        return name.equals(group.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
