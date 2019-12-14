package cn.hamster3.service.spigot.listener;

import cn.hamster3.service.spigot.HamsterService;
import cn.hamster3.service.spigot.event.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ServiceConnectListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onServiceConnect(ServiceConnectEvent event) {
        HamsterService.log("尝试连接至服务器 [%s:%d]", HamsterService.getServiceHost(), HamsterService.getServicePort());
    }

    @EventHandler(ignoreCancelled = true)
    public void onServiceConnected(ServiceConnectedEvent event) {
        if (event.isSuccess()) {
            HamsterService.log("服务器连接成功!");
        } else {
            HamsterService.log("服务器连接失败: " + event.getCause());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onServiceReconnect(ServiceReconnectEvent event) {
        HamsterService.log("尝试重新连接至服务器...");
    }

    @EventHandler(ignoreCancelled = true)
    public void onServiceDisconnect(ServiceDisconnectEvent event) {
        HamsterService.log("正在断开与服务器的连接...");
    }

    @EventHandler(ignoreCancelled = true)
    public void onServiceDisconnected(ServiceDisconnectedEvent event) {
        HamsterService.log("已断开与服务器的连接.");
    }
}
