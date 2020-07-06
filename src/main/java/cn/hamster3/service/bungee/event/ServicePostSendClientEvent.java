package cn.hamster3.service.bungee.event;

import cn.hamster3.service.bungee.service.ServiceConnection;
import cn.hamster3.service.bungee.service.ServiceGroup;

/**
 * 消息发送给客户端之后产生的事件
 */
public class ServicePostSendClientEvent extends ServiceGroupMessageEvent {
    private final ServiceConnection connection;

    private final boolean success;
    private Throwable cause;

    public ServicePostSendClientEvent(String message, ServiceGroup group, ServiceConnection connection) {
        super(message, group);
        this.connection = connection;
        success = true;
    }

    public ServicePostSendClientEvent(String tag, String message, ServiceGroup group, ServiceConnection connection) {
        super(tag, message, group);
        this.connection = connection;
        success = true;
    }

    public ServicePostSendClientEvent(String message, ServiceGroup group, ServiceConnection connection, Throwable cause) {
        super(message, group);
        this.connection = connection;
        this.cause = cause;
        success = false;
    }

    public ServicePostSendClientEvent(String tag, String message, ServiceGroup group, ServiceConnection connection, Throwable cause) {
        super(tag, message, group);
        this.connection = connection;
        this.cause = cause;
        success = false;
    }

    public ServiceConnection getConnection() {
        return connection;
    }

    public boolean isSuccess() {
        return success;
    }

    public Throwable getCause() {
        return cause;
    }
}
