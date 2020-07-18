package cn.hamster3.service.bungee.listener;

import cn.hamster3.service.bungee.service.ServiceGroup;
import cn.hamster3.service.bungee.service.ServiceManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerConnected(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        for (ServiceGroup group : ServiceManager.getGroups()) {
            group.broadcast("HamsterService", "playerConnected %s %s", player.getUniqueId(), player.getName().toLowerCase());
        }
    }

    @EventHandler
    public void onPlayerDisconnected(ServerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        for (ServiceGroup group : ServiceManager.getGroups()) {
            group.broadcast("HamsterService", "playerDisconnected %s %s", player.getUniqueId(), player.getName().toLowerCase());
        }
    }


}
