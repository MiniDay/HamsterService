package cn.hamster3.service.bungee.service;

import cn.hamster3.service.bungee.BungeeService;
import cn.hamster3.service.bungee.event.ServiceClientDisconnectedEvent;
import cn.hamster3.service.bungee.event.ServiceClientRegisterEvent;
import cn.hamster3.service.bungee.event.ServiceGroupReceiveEvent;
import cn.hamster3.service.spigot.HamsterService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;

class ServiceReadHandler extends SimpleChannelInboundHandler<String> {
    private ServiceGroup group;
    private ServiceConnection connection;

    public ServiceReadHandler(ServiceGroup group, ServiceConnection connection) {
        this.group = group;
        this.connection = connection;
    }

    private static String getServerName(InetSocketAddress address) {
        for (ServerInfo info : ProxyServer.getInstance().getServers().values()) {
            if (info.getAddress().equals(address)) {
                return info.getName();
            }
        }
        return null;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, String msg) {
        if (!connection.isRegistered()) {
            if (!msg.startsWith("HamsterService:")) {
                connection.disconnect();
                ServiceClientRegisterEvent event = new ServiceClientRegisterEvent(group, connection, false,
                        "未验证服务器信息");
                ProxyServer.getInstance().getPluginManager().callEvent(event);
                return;
            }
        }
        ServiceGroupReceiveEvent event = new ServiceGroupReceiveEvent(msg, group, connection);
        ProxyServer.getInstance().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        if (!event.hasTag()) {
            group.broadcast(event.getMessage());
            return;
        }
        if (event.getTag().equalsIgnoreCase("HamsterService")) {
            execute(event.getMessage().split(" "));
            return;
        }
        group.broadcast(event.getTag(), event.getMessage());
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        context.close();
        group.removeConnection(connection);
        ServiceClientDisconnectedEvent disconnectionEvent = new ServiceClientDisconnectedEvent(group, connection);
        ProxyServer.getInstance().getPluginManager().callEvent(disconnectionEvent);
        BungeeService.warning("服务器 %s 断开了与 服务组 %s 的连接!", connection.getName(), group.getName());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        HamsterService.warning("与客户端 %s 的通信中出现了一个错误: ", connection.getName());
        cause.printStackTrace();
    }

    private void execute(String[] args) {
        if (args[0].equalsIgnoreCase("register")) {
            if (connection.isRegistered()) {
                return;
            }
            try {
                if (!group.getPassword().equals(args[1])) {
                    connection.sendMessage("HamsterService", "registerFailed 验证密码错误");
                    connection.disconnect();
                    ServiceClientRegisterEvent event = new ServiceClientRegisterEvent(group, connection, false,
                            "验证密码错误");
                    ProxyServer.getInstance().getPluginManager().callEvent(event);
                    return;
                }
                InetSocketAddress address = new InetSocketAddress(args[2], Integer.parseInt(args[3]));
                String name = getServerName(address);
                if (name == null) {
                    connection.sendMessage("HamsterService", "registerFailed 未被BC注册为子服");
                    connection.disconnect();
                    ServiceClientRegisterEvent event = new ServiceClientRegisterEvent(group, connection, false,
                            String.format("地址 %s:%d 未被注册为合法的子服", address.getHostString(), address.getPort()));
                    ProxyServer.getInstance().getPluginManager().callEvent(event);
                    return;
                }
                connection.setName(name);
                connection.setBukkitPort(address.getPort());
                connection.setBukkitAddress(address.getHostString());
                connection.setRegistered();
                group.addConnection(connection);
                connection.sendMessage("HamsterService", "registered " + name);
                ProxyServer.getInstance().getPluginManager().callEvent(new ServiceClientRegisterEvent(group, connection, true));
            } catch (Exception e) {
                connection.sendMessage("HamsterService", "registerFailed 验证参数不正确");
                connection.disconnect();
                ServiceClientRegisterEvent event = new ServiceClientRegisterEvent(group, connection, false,
                        "验证参数不正确");
                ProxyServer.getInstance().getPluginManager().callEvent(event);
            }
        }
    }
}
