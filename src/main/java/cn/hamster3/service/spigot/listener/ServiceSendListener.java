package cn.hamster3.service.spigot.listener;

import cn.hamster3.service.spigot.HamsterService;
import cn.hamster3.service.spigot.event.ServicePostSendEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ServiceSendListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onServiceSend(ServicePostSendEvent event) {
        if (event.hasTag()) {
            HamsterService.log("[out] %s: %s", event.getTag(), event.getMessage());
        } else {
            HamsterService.log("[out] " + event.getMessage());
        }
    }
}
