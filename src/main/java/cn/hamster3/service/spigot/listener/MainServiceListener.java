package cn.hamster3.service.spigot.listener;

import cn.hamster3.service.spigot.HamsterService;
import cn.hamster3.service.spigot.event.ServiceReceiveEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MainServiceListener implements Listener {
    private HamsterService plugin;

    public MainServiceListener(HamsterService plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onServiceReceive(ServiceReceiveEvent event) {
        if (!event.hasTag()) {
            return;
        }
        if (!event.getTag().equals("HamsterService")) {
            return;
        }
        String[] args = event.getMessage().split(" ");
        if ("command".equals(args[0])) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), event.getMessage().substring(8)), 1);
        }
    }
}
