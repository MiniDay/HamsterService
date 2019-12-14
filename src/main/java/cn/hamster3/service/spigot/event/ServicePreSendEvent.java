package cn.hamster3.service.spigot.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class ServicePreSendEvent extends ServiceMessageEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    public ServicePreSendEvent(String message) {
        super(message);
        cancelled = false;
    }

    public ServicePreSendEvent(String tag, String message) {
        super(tag, message);
        cancelled = false;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
