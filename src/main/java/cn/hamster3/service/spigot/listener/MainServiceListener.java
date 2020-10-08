package cn.hamster3.service.spigot.listener;

import cn.hamster3.service.spigot.HamsterService;
import cn.hamster3.service.spigot.event.ServiceReceiveEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class MainServiceListener implements Listener {
    private final HamsterService plugin;
    private final HashSet<UUID> onlinePlayerUUID;
    private final HashSet<String> onlinePlayerName;
    private final HashMap<String, UUID> playerUUID;
    private final HashMap<UUID, String> playerServerUUID;
    private final HashMap<String, String> playerServerName;

    public MainServiceListener(HamsterService plugin) {
        this.plugin = plugin;
        playerUUID = new HashMap<>();
        onlinePlayerUUID = new HashSet<>();
        onlinePlayerName = new HashSet<>();
        playerServerUUID = new HashMap<>();
        playerServerName = new HashMap<>();
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
            case "playerPostLogin": {
                UUID uuid = UUID.fromString(args[1]);
                onlinePlayerUUID.add(uuid);
                onlinePlayerName.add(args[2]);
                playerUUID.put(args[2], uuid);
                break;
            }
            case "playerConnected": {
                UUID uuid = UUID.fromString(args[1]);
                onlinePlayerUUID.add(uuid);
                onlinePlayerName.add(args[2]);
                playerServerUUID.put(uuid, args[3]);
                playerServerName.put(args[2], args[3]);
                playerUUID.put(args[2], uuid);
                break;
            }
            case "playerDisconnected": {
                onlinePlayerUUID.remove(UUID.fromString(args[1]));
                onlinePlayerName.remove(args[2]);
                break;
            }
            case "executeConsoleCommand": {
                StringBuilder builder = new StringBuilder(args[2]);
                for (int i = 3; i < args.length; i++) {
                    builder.append(' ').append(args[i]);
                }
                Bukkit.dispatchCommand(
                        Bukkit.getConsoleSender(),
                        builder.toString()
                );
            }
        }
    }

    public HashSet<UUID> getOnlinePlayerUUID() {
        return onlinePlayerUUID;
    }

    public HashSet<String> getOnlinePlayerName() {
        return onlinePlayerName;
    }

    public String getPlayingServer(UUID uuid) {
        return playerServerUUID.get(uuid);
    }

    public String getPlayingServer(String name) {
        return playerServerName.get(name);
    }

    public UUID getPlayerUUID(String name) {
        return playerUUID.get(name);
    }
}
