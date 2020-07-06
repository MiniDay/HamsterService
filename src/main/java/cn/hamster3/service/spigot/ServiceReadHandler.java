package cn.hamster3.service.spigot;

import cn.hamster3.service.spigot.event.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.bukkit.Bukkit;

class ServiceReadHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        ServiceReceiveEvent event = new ServiceReceiveEvent(msg);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.hasTag()) {
            return;
        }
        if (!event.getTag().equalsIgnoreCase("HamsterService")) {
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
        HamsterService.reconnect();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        HamsterService.warning("与服务端的通信中出现了一个错误: ");
        cause.printStackTrace();
    }

    private void execute(String[] args) {
        if (args[0].equalsIgnoreCase("registered")) {
            HamsterService.setName(args[1]);
            Bukkit.getPluginManager().callEvent(new ServicePostRegisterEvent());

            // 把未发送成功的消息发送回去
            for (ServicePreSendEvent event : HamsterService.messages) {
                HamsterService.sendMessage(event);
            }
            HamsterService.messages.clear();
        } else if (args[0].equalsIgnoreCase("registerFailed")) {
            Bukkit.getPluginManager().callEvent(new ServicePostRegisterEvent(args[1]));
        }
    }
}
