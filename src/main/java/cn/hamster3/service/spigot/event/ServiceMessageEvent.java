package cn.hamster3.service.spigot.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.nio.charset.StandardCharsets;

public class ServiceMessageEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private String tag;
    private String message;

    public ServiceMessageEvent(String message) {
        super(true);
        setMessage(message);
    }

    public ServiceMessageEvent(String tag, String message) {
        super(true);
        if (tag == null) {
            setMessage(message);
            return;
        }
        if (!tag.matches("[a-zA-Z0-9_]*")) {
            throw new IllegalArgumentException("tag 只能使用字母、数字或下划线!");
        }
        if ((tag + ":" + message).getBytes(StandardCharsets.UTF_8).length >= 0xffff) {
            throw new IllegalArgumentException("消息总长度不能超过 65535 字节!");
        }
        this.tag = tag;
        setMessage(message);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean hasTag() {
        return tag != null;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        if (tag == null) {
            this.tag = null;
            return;
        }
        if (!tag.matches("[a-zA-Z0-9_]*")) {
            throw new IllegalArgumentException("tag 只能使用字母、数字或下划线!");
        }
        if ((tag + ":" + message).getBytes(StandardCharsets.UTF_8).length >= 0xffff) {
            throw new IllegalArgumentException("消息总长度不能超过 65535 字节!");
        }
        this.tag = tag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (message == null) {
            throw new IllegalArgumentException("消息不能被设置为 null !");
        }
        int length = 0;
        if (tag != null) {
            length += tag.getBytes(StandardCharsets.UTF_8).length;
            length += ":".getBytes(StandardCharsets.UTF_8).length;
        }
        length += message.getBytes(StandardCharsets.UTF_8).length;
        if (length >= 0xffff) {
            throw new IllegalArgumentException("消息总长度不能超过 65535 字节!");
        }
        this.message = message;
        if (tag == null) {
            resetTag();
        }
    }

    private void resetTag() {
        if (!message.contains(":")) {
            return;
        }
        String[] args = message.split(":", 2);
        if (args[0].matches("[a-zA-Z0-9_]*")) {
            tag = args[0];
            message = args[1];
        }
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
