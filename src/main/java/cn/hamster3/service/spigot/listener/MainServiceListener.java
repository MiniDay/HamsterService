package cn.hamster3.service.spigot.listener;

import cn.hamster3.service.spigot.HamsterService;
import cn.hamster3.service.spigot.event.ServiceReceiveEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.UUID;

public class MainServiceListener implements Listener {
    private final HamsterService plugin;
    private final HashSet<UUID> onlinePlayerUUID;
    private final HashSet<String> onlinePlayerName;

    public MainServiceListener(HamsterService plugin) {
        this.plugin = plugin;
        onlinePlayerUUID = new HashSet<>();
        onlinePlayerName = new HashSet<>();
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
            case "playerConnected": {
                onlinePlayerUUID.add(UUID.fromString(args[1]));
                onlinePlayerName.add(args[2]);
                break;
            }
            case "playerDisconnected": {
                onlinePlayerUUID.remove(UUID.fromString(args[1]));
                onlinePlayerName.remove(args[2]);
                break;
            }
        }
    }

    public HashSet<UUID> getOnlinePlayerUUID() {
        return onlinePlayerUUID;
    }

    public HashSet<String> getOnlinePlayerName() {
        return onlinePlayerName;
    }
}
