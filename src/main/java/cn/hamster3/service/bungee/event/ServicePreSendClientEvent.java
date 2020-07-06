package cn.hamster3.service.bungee.event;

import cn.hamster3.service.bungee.service.ServiceConnection;
import cn.hamster3.service.bungee.service.ServiceGroup;
import net.md_5.bungee.api.plugin.Cancellable;

/**
 * 消息发送给客户端之前产生的事件
 */
public class ServicePreSendClientEvent extends ServiceGroupMessageEvent implements Cancellable {
    private final ServiceConnection connection;

    private boolean cancelled;

    public ServicePreSendClientEvent(String message, ServiceGroup group, ServiceConnection connection) {
        super(message, group);
        this.connection = connection;
        cancelled = false;
    }

    public ServicePreSendClientEvent(String tag, String message, ServiceGroup group, ServiceConnection connection) {
        super(tag, message, group);
        this.connection = connection;
        cancelled = false;
    }

    public ServiceConnection getConnection() {
        return connection;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
