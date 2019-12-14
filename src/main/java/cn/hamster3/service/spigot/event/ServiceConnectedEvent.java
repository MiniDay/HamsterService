package cn.hamster3.service.spigot.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServiceConnectedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private boolean success;
    private Throwable cause;

    public ServiceConnectedEvent() {
        success = true;
    }

    public ServiceConnectedEvent(Throwable cause) {
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
