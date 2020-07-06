package cn.hamster3.service.spigot.listener;

import cn.hamster3.service.spigot.HamsterService;
import cn.hamster3.service.spigot.event.ServicePreRegisterEvent;
import cn.hamster3.service.spigot.event.ServicePostRegisterEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ServiceRegisterListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onServiceRegister(ServicePreRegisterEvent event) {
        HamsterService.log("发送注册信息.");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onServiceRegistered(ServicePostRegisterEvent event) {
        if (event.isSuccess()) {
            HamsterService.log("注册成功! 服务器名称为: " + HamsterService.getServerName());
        } else {
            HamsterService.warning("注册失败: " + event.getCause());
        }
    }
}
