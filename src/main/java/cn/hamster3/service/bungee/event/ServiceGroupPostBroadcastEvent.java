package cn.hamster3.service.bungee.event;

import cn.hamster3.service.bungee.service.ServiceGroup;

public class ServiceGroupPostBroadcastEvent extends ServiceGroupMessageEvent {
    public ServiceGroupPostBroadcastEvent(String message, ServiceGroup group) {
        super(message, group);
    }

    public ServiceGroupPostBroadcastEvent(String tag, String message, ServiceGroup group) {
        super(tag, message, group);
    }
}
