package cn.hamster3.service.spigot.event;

import org.bukkit.event.HandlerList;

public class ServicePostSendEvent extends ServiceMessageEvent {
    private static final HandlerList handlers = new HandlerList();

    private boolean success;
    private Throwable cause;

    public ServicePostSendEvent(String message) {
        super(message);
    }

    public ServicePostSendEvent(String tag, String message) {
        super(tag, message);
    }

    public ServicePostSendEvent(String message, Throwable cause) {
        super(message);
        this.cause = cause;
        success = false;
    }

    public ServicePostSendEvent(String tag, String message, Throwable cause) {
        super(tag, message);
        this.cause = cause;
        success = false;
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
