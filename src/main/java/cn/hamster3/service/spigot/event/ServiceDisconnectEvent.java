package cn.hamster3.service.spigot.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServiceDisconnectEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public ServiceDisconnectEvent() {
        super(true);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
