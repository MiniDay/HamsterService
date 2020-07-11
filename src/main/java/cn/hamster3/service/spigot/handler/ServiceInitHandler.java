package cn.hamster3.service.spigot.handler;

import cn.hamster3.service.spigot.event.ServicePreSendEvent;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ServiceInitHandler extends ChannelInitializer<NioSocketChannel> {
    // 当连接因异常断开时, 要发送的消息将会被存在这里, 等待服务器重新连接后再发送.
    public static ArrayList<ServicePreSendEvent> messages;
    private static String serverName;
    private static String groupName;
    private final String serviceHost;
    private final int servicePort;
    private final String servicePassword;

    public ServiceInitHandler(String serviceHost, int servicePort, String servicePassword) {
        messages = new ArrayList<>();
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
        this.servicePassword = servicePassword;
    }

    @Override
    protected void initChannel(NioSocketChannel channel) {
        channel.pipeline()
                .addLast(new LengthFieldPrepender(4))
                .addLast(new LengthFieldBasedFrameDecoder(0xffff, 0, 4, 0, 4))
                .addLast(new StringDecoder(StandardCharsets.UTF_8))
                .addLast(new StringEncoder(StandardCharsets.UTF_8))
                .addLast(new ServiceReadHandler(serviceHost, servicePort, servicePassword, this))
        ;
    }

    public ArrayList<ServicePreSendEvent> getMessages() {
        return messages;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        ServiceInitHandler.serverName = serverName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        ServiceInitHandler.groupName = groupName;
    }
}
