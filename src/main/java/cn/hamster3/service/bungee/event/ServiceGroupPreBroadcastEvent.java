package cn.hamster3.service.bungee.event;

import cn.hamster3.service.bungee.service.ServiceGroup;
import net.md_5.bungee.api.plugin.Cancellable;

public class ServiceGroupPreBroadcastEvent extends ServiceMessageEvent implements Cancellable {
    private boolean cancelled;

    public ServiceGroupPreBroadcastEvent(String message, ServiceGroup group) {
        super(message, group);
        cancelled = false;
    }

    public ServiceGroupPreBroadcastEvent(String tag, String message, ServiceGroup group) {
        super(tag, message, group);
        cancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
