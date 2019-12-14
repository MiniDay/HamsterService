package cn.hamster3.service.bungee.event;

import cn.hamster3.service.bungee.service.ServiceConnection;
import cn.hamster3.service.bungee.service.ServiceGroup;
import net.md_5.bungee.api.plugin.Event;

public class ServiceClientDisconnectedEvent extends Event {
    private ServiceGroup group;
    private ServiceConnection connection;

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
