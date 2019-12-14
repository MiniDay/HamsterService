package cn.hamster3.service.bungee.event;

import cn.hamster3.service.bungee.service.ServiceGroup;

public class ServiceGroupBroadcastEvent extends ServiceMessageEvent {
    public ServiceGroupBroadcastEvent(String message, ServiceGroup group) {
        super(message, group);
    }

    public ServiceGroupBroadcastEvent(String tag, String message, ServiceGroup group) {
        super(tag, message, group);
    }
}
