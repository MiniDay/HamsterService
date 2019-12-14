package cn.hamster3.service.bungee.listener;

import cn.hamster3.service.bungee.BungeeService;
import cn.hamster3.service.bungee.event.ServiceGroupCloseEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class GroupCloseListener implements Listener {
    @EventHandler
    public void onServiceGroupClose(ServiceGroupCloseEvent event) {
        if (event.isSuccess()) {
            BungeeService.log("[*] 服务组 %s 在地址 %s:%d 上关闭成功!",
                    event.getGroup().getName(),
                    event.getGroup().getHost(),
                    event.getGroup().getPort());
        } else {
            BungeeService.warning("[*] 服务组 %s 在地址 %s:%d 上关闭时出现异常: %s",
                    event.getGroup().getName(),
                    event.getGroup().getHost(),
                    event.getGroup().getPort(),
                    event.getCause()
            );
        }
    }
}
