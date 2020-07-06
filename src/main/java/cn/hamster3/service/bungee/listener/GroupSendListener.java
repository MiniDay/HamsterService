package cn.hamster3.service.bungee.listener;

import cn.hamster3.service.bungee.BungeeService;
import cn.hamster3.service.bungee.event.ServicePostSendClientEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class GroupSendListener implements Listener {
    @EventHandler
    public void onServiceSend(ServicePostSendClientEvent event) {
        if (event.isSuccess()) {
            if (event.hasTag()) {
                BungeeService.log("[%s] --->> [%s] %s: %s",
                        event.getGroup().getName(),
                        event.getConnection().getName(),
                        event.getTag(),
                        event.getMessage());
            } else {
                BungeeService.log("[%s] --->> [%s] %s",
                        event.getGroup().getName(),
                        event.getConnection().getName(),
                        event.getMessage());
            }
        } else {
            if (event.hasTag()) {
                BungeeService.warning("[发送失败] [%s] --->> [%s] %s: %s",
                        event.getGroup().getName(),
                        event.getConnection().getName(),
                        event.getTag(),
                        event.getMessage());
            } else {
                BungeeService.warning("[发送失败] [%s] --->> [%s] %s",
                        event.getGroup().getName(),
                        event.getConnection().getName(),
                        event.getMessage());
            }
            BungeeService.warning("失败原因: " + event.getCause().toString());
        }
    }
}
