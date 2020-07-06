package cn.hamster3.service.bungee.event;

import cn.hamster3.service.bungee.service.ServiceGroup;
import net.md_5.bungee.api.plugin.Event;

import java.nio.charset.StandardCharsets;

/**
 * 服务组消息事件的基类
 */
public class ServiceGroupMessageEvent extends Event {
    private String tag;
    private String message;

    private ServiceGroup group;

    public ServiceGroupMessageEvent(String message, ServiceGroup group) {
        setMessage(message);
        this.group = group;
    }

    public ServiceGroupMessageEvent(String tag, String message, ServiceGroup group) {
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
        this.group = group;
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

    public ServiceGroup getGroup() {
        return group;
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
}
