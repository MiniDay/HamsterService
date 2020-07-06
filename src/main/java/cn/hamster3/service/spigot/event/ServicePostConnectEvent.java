package cn.hamster3.service.spigot.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServicePostConnectEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final boolean success;
    private Throwable cause;

    public ServicePostConnectEvent() {
        super(true);
        success = true;
    }

    public ServicePostConnectEvent(Throwable cause) {
        super(true);
        success = false;
        this.cause = cause;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean isSuccess() {
        return success;
    }

    public Throwable getCause() {
        return cause;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
