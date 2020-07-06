package cn.hamster3.service.bungee.event;

import cn.hamster3.service.bungee.service.ServiceConnection;
import cn.hamster3.service.bungee.service.ServiceGroup;
import net.md_5.bungee.api.plugin.Event;

/**
 * 客户端从桥接组上断开的事件
 */
public class ServiceClientDisconnectedEvent extends Event {
    private final ServiceGroup group;
    private final ServiceConnection connection;

    public ServiceClientDisconnectedEvent(ServiceGroup group, ServiceConnection connection) {
        this.group = group;
        this.connection = connection;
    }

    public ServiceGroup getGroup() {
        return group;
    }

    public ServiceConnection getConnection() {
        return connection;
    }
}
