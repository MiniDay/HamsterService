package cn.hamster3.service.bungee.event;

import cn.hamster3.service.bungee.service.ServiceConnection;
import cn.hamster3.service.bungee.service.ServiceGroup;
import net.md_5.bungee.api.plugin.Event;

/**
 * 客户端在桥接组上注册后产生的事件
 */
public class ServiceClientRegisterEvent extends Event {
    private final ServiceGroup group;
    private final ServiceConnection connection;

    private final boolean success;
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
