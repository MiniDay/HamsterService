package cn.hamster3.service.spigot.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServiceConnectEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public ServiceConnectEvent(boolean isAsync) {
        super(isAsync);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}