package cn.hamster3.service.bungee.listener;

import cn.hamster3.service.bungee.BungeeService;
import cn.hamster3.service.bungee.event.ServiceGroupReceiveEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class GroupReceiveListener implements Listener {
    @EventHandler
    public void onServiceGroupReceive(ServiceGroupReceiveEvent event) {
        if (event.hasTag()) {
            BungeeService.log("[%s] <<--- [%s] %s: %s",
                    event.getGroup().getName(),
                    event.getConnection().getName(),
                    event.getTag(),
                    event.getMessage());
        } else {
            BungeeService.log("[%s] <<--- [%s] %s",
                    event.getGroup().getName(),
                    event.getConnection().getName(),
                    event.getMessage());
        }
    }
}
