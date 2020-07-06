package cn.hamster3.service.spigot.listener;

import cn.hamster3.service.spigot.HamsterService;
import cn.hamster3.service.spigot.event.ServiceReceiveEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class MainServiceListener implements Listener {
    private final HamsterService plugin;

    public MainServiceListener(HamsterService plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onServiceReceive(ServiceReceiveEvent event) {
        if (!event.hasTag()) {
            return;
        }
        if (!event.getTag().equals("HamsterService")) {
            return;
        }
        String[] args = event.getMessage().split(" ");
        switch (args[0]) {
            case "command": {
                Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), event.getMessage().substring(8)), 1);
                break;
            }
            case "sendMessage": {
                Player player = Bukkit.getPlayer(UUID.fromString(args[1]));
                if (player == null) {
                    break;
                }
                // "sendMessage 8311709b-1550-3cb6-bb6e-1d7f72d2d304 消息内容";
                // 第49个字符之后为消息内容
                player.sendMessage(event.getMessage().substring(49));
                break;
            }
        }
    }
}
