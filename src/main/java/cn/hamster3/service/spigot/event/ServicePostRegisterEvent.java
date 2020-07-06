package cn.hamster3.service.spigot.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServicePostRegisterEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final boolean success;
    private String cause;

    public ServicePostRegisterEvent() {
        super(true);
        success = true;
    }

    public ServicePostRegisterEvent(String cause) {
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

    public String getCause() {
        return cause;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
