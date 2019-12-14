package cn.hamster3.service.spigot.listener;

import cn.hamster3.service.spigot.HamsterService;
import cn.hamster3.service.spigot.event.ServiceRegisterEvent;
import cn.hamster3.service.spigot.event.ServiceRegisteredEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ServiceRegisterListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onServiceRegister(ServiceRegisterEvent event) {
        HamsterService.log("发送注册信息.");
    }

    @EventHandler(ignoreCancelled = true)
    public void onServiceRegistered(ServiceRegisteredEvent event) {
        if (event.isSuccess()) {
            HamsterService.log("注册成功! 服务器名称为: " + HamsterService.getServerName());
        } else {
            HamsterService.warning("注册失败: " + event.getCause());
        }
    }
}
