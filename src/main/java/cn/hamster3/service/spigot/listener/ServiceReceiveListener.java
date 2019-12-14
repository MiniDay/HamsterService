package cn.hamster3.service.spigot.listener;

import cn.hamster3.service.spigot.HamsterService;
import cn.hamster3.service.spigot.event.ServiceReceiveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ServiceReceiveListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onServiceReceive(ServiceReceiveEvent event) {
        if (event.hasTag()) {
            HamsterService.log("[in] %s: %s", event.getTag(), event.getMessage());
        } else {
            HamsterService.log("[in] " + event.getMessage());
        }
    }
}
