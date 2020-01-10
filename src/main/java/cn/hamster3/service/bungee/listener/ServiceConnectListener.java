package cn.hamster3.service.bungee.listener;

import cn.hamster3.service.bungee.BungeeService;
import cn.hamster3.service.bungee.event.ServiceClientConnectedEvent;
import cn.hamster3.service.bungee.event.ServiceClientDisconnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServiceConnectListener implements Listener {
    @EventHandler
    public void onServiceConnected(ServiceClientConnectedEvent event) {
        BungeeService.log("[+] [%s] <==== [%s:%d]",
                event.getGroup().getName(),
                event.getConnection().getConnectionAddress().getHostString(),
                event.getConnection().getConnectionAddress().getPort());
    }

    @EventHandler
    public void onServiceDisconnected(ServiceClientDisconnectedEvent event) {
        BungeeService.log("[-] [%s] <==== [%s]",
                event.getGroup().getName(),
                event.getConnection().getName());
    }
}
