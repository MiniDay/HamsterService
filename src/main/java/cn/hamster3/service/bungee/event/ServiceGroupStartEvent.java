package cn.hamster3.service.bungee.event;

import cn.hamster3.service.bungee.service.ServiceGroup;
import net.md_5.bungee.api.plugin.Event;

/**
 * 桥接组启动事件
 */
public class ServiceGroupStartEvent extends Event {
    private ServiceGroup group;
    private boolean success;
    private Throwable cause;

    public ServiceGroupStartEvent(ServiceGroup group, boolean success) {
        this.group = group;
        this.success = success;
    }

    public ServiceGroupStartEvent(ServiceGroup group, boolean success, Throwable cause) {
        this.group = group;
        this.success = success;
        this.cause = cause;
    }

    public ServiceGroup getGroup() {
        return group;
    }

    public boolean isSuccess() {
        return success;
    }

    public Throwable getCause() {
        return cause;
    }
}
