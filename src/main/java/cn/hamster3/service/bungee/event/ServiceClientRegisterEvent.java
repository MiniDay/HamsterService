package cn.hamster3.service.bungee.event;

import cn.hamster3.service.bungee.service.ServiceConnection;
import cn.hamster3.service.bungee.service.ServiceGroup;
import net.md_5.bungee.api.plugin.Event;

public class ServiceClientRegisterEvent extends Event {
    private ServiceGroup group;
    private ServiceConnection connection;

    private boolean success;
    private String cause;

    public ServiceClientRegisterEvent(ServiceGroup group, ServiceConnection connection, boolean success) {
        this.group = group;
        this.connection = connection;
        this.success = success;
    }

    public ServiceClientRegisterEvent(ServiceGroup group, ServiceConnection connection, boolean success, String cause) {
        this.group = group;
        this.connection = connection;
        this.success = success;
        this.cause = cause;
    }

    public ServiceGroup getGroup() {
        return group;
    }

    public ServiceConnection getConnection() {
        return connection;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getCause() {
        return cause;
    }
}
