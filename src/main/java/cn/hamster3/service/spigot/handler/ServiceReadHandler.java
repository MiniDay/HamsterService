package cn.hamster3.service.spigot.handler;

import cn.hamster3.service.spigot.HamsterService;
import cn.hamster3.service.spigot.event.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.bukkit.Bukkit;

public class ServiceReadHandler extends SimpleChannelInboundHandler<String> {
    private final String serviceHost;
    private final int servicePort;
    private final String servicePassword;

    private final ServiceInitHandler initHandler;

    public ServiceReadHandler(String serviceHost, int servicePort, String servicePassword, ServiceInitHandler initHandler) {
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
        this.servicePassword = servicePassword;
        this.initHandler = initHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        ServiceReceiveEvent event = new ServiceReceiveEvent(msg);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        if (!"HamsterService".equals(event.getTag())) {
            return;
        }
        execute(event.getMessage().split(" "));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Bukkit.getPluginManager().callEvent(new ServicePreDisconnectEvent());
        ctx.close().addListener(future -> {
            if (future.isSuccess()) {
                Bukkit.getPluginManager().callEvent(new ServicePostDisconnectEvent());
            } else {
                Bukkit.getPluginManager().callEvent(new ServicePostDisconnectEvent(future.cause()));
            }
        });
        HamsterService.reconnect(serviceHost, servicePort, servicePassword);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        HamsterService.warning("与服务端的通信中出现了一个错误: ");
        cause.printStackTrace();
    }

    private void execute(String[] args) {
        if (args[0].equalsIgnoreCase("registered")) {
            // 成功注册
            initHandler.setServerName(args[1]);
            initHandler.setGroupName(args[2]);
            Bukkit.getPluginManager().callEvent(new ServicePostRegisterEvent());

            // 把未发送成功的消息发送回去
            for (ServicePreSendEvent event : initHandler.getMessages()) {
                HamsterService.sendMessage(event);
            }
            initHandler.getMessages().clear();
            return;
        }
        if (args[0].equalsIgnoreCase("registerFailed")) {
            // 注册失败
            Bukkit.getPluginManager().callEvent(new ServicePostRegisterEvent(args[1]));
        }
    }
}
