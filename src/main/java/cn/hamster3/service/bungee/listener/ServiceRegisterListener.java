package cn.hamster3.service.bungee.listener;

import cn.hamster3.service.bungee.BungeeService;
import cn.hamster3.service.bungee.event.ServiceClientRegisterEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServiceRegisterListener implements Listener {
    @EventHandler
    public void onServiceRegister(ServiceClientRegisterEvent event) {
        if (event.isSuccess()) {
            BungeeService.log("[*] 服务器 %s 在服务组 %s 上注册成功 ----> %s:%d",
                    event.getConnection().getName(),
                    event.getGroup().getName(),
                    event.getConnection().getBukkitAddress(),
                    event.getConnection().getBukkitPort());
        } else {
            BungeeService.warning("[*] 由于 %s 而断开了一个在服务组 %s 上的非法连接 ----> %s:%d",
                    event.getCause(),
                    event.getGroup().getName(),
                    event.getConnection().getConnectionAddress().getHostString(),
                    event.getConnection().getConnectionAddress().getPort());
        }
    }
}
