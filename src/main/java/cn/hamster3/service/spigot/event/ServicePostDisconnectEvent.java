package cn.hamster3.service.spigot.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServicePostDisconnectEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final boolean success;
    private Throwable cause;

    public ServicePostDisconnectEvent() {
        super(true);
        success = true;
    }

    public ServicePostDisconnectEvent(Throwable cause) {
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
