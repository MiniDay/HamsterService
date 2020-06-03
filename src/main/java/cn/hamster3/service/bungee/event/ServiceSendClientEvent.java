package cn.hamster3.service.bungee.event;

import cn.hamster3.service.bungee.service.ServiceConnection;
import cn.hamster3.service.bungee.service.ServiceGroup;

public class ServiceSendClientEvent extends ServiceMessageEvent {
    private final ServiceConnection connection;

    private final boolean success;
    private Throwable cause;

    public ServiceSendClientEvent(String message, ServiceGroup group, ServiceConnection connection) {
        super(message, group);
        this.connection = connection;
        success = true;
    }

    public ServiceSendClientEvent(String tag, String message, ServiceGroup group, ServiceConnection connection) {
        super(tag, message, group);
        this.connection = connection;
        success = true;
    }

    public ServiceSendClientEvent(String message, ServiceGroup group, ServiceConnection connection, Throwable cause) {
        super(message, group);
        this.connection = connection;
        this.cause = cause;
        success = false;
    }

    public ServiceSendClientEvent(String tag, String message, ServiceGroup group, ServiceConnection connection, Throwable cause) {
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
