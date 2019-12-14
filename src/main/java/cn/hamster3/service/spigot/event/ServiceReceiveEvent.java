package cn.hamster3.service.spigot.event;

import org.bukkit.event.HandlerList;

public class ServiceReceiveEvent extends ServiceMessageEvent {
    private static final HandlerList handlers = new HandlerList();

    public ServiceReceiveEvent(String message) {
        super(message);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
