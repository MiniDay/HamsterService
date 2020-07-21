package cn.hamster3.service.bungee.listener;

import cn.hamster3.service.bungee.event.ServiceClientRegisterEvent;
import cn.hamster3.service.bungee.service.ServiceGroup;
import cn.hamster3.service.bungee.service.ServiceManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServicePlayerListener implements Listener {
    private final HashMap<UUID, String> onlinePlayer;

    public ServicePlayerListener() {
        onlinePlayer = new HashMap<>();
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        onlinePlayer.put(player.getUniqueId(), player.getName());
        for (ServiceGroup group : ServiceManager.getGroups()) {
            group.broadcast("HamsterService", "playerPostLogin %s %s", player.getUniqueId(), player.getName().toLowerCase());
        }
    }

    @EventHandler
    public void onPlayerConnected(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        onlinePlayer.put(player.getUniqueId(), player.getName());
        for (ServiceGroup group : ServiceManager.getGroups()) {
            group.broadcast("HamsterService", "playerConnected %s %s %s", player.getUniqueId(), player.getName().toLowerCase(), event.getServer().getInfo().getName());
        }
    }

    @EventHandler
    public void onPlayerDisconnected(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        onlinePlayer.remove(player.getUniqueId());
        for (ServiceGroup group : ServiceManager.getGroups()) {
            group.broadcast("HamsterService", "playerDisconnected %s %s", player.getUniqueId(), player.getName().toLowerCase());
        }
    }

    @EventHandler
    public void onServiceClientConnected(ServiceClientRegisterEvent event) {
        for (Map.Entry<UUID, String> entry : onlinePlayer.entrySet()) {
            event.getConnection().sendMessage("HamsterService", String.format("playerConnected %s %s", entry.getKey(), entry.getValue().toLowerCase()));
        }
    }
}
