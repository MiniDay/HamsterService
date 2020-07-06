package cn.hamster3.service.bungee.listener;

import cn.hamster3.service.bungee.BungeeService;
import cn.hamster3.service.bungee.event.ServiceGroupPostBroadcastEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class GroupBroadcastListener implements Listener {
    @EventHandler
    public void onServiceGroupBroadcast(ServiceGroupPostBroadcastEvent event) {
        if (event.hasTag()) {
            BungeeService.log("[%s] >>>>> %s: %s",
                    event.getGroup().getName(),
                    event.getTag(),
                    event.getMessage());
        } else {
            BungeeService.log("[%s] >>>>> %s",
                    event.getGroup().getName(),
                    event.getMessage());
        }
    }
}
