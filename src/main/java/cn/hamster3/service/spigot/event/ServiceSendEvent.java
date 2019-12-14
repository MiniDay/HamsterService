package cn.hamster3.service.spigot.event;

import org.bukkit.event.HandlerList;

public class ServiceSendEvent extends ServiceMessageEvent {
    private static final HandlerList handlers = new HandlerList();

    private boolean success;
    private Throwable cause;

    public ServiceSendEvent(String message) {
        super(message);
    }

    public ServiceSendEvent(String tag, String message) {
        super(tag, message);
    }

    public ServiceSendEvent(String message, Throwable cause) {
        super(message);
        this.cause = cause;
        success = false;
    }

    public ServiceSendEvent(String tag, String message, Throwable cause) {
        super(tag, message);
        this.cause = cause;
        success = false;
    }

    public boolean isSuccess() {
        return success;
    }

    public Throwable getCause() {
        return cause;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
