package cn.hamster3.service.bungee.service;

import cn.hamster3.service.bungee.event.ServiceClientConnectedEvent;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import net.md_5.bungee.api.ProxyServer;

import java.nio.charset.StandardCharsets;

class ServiceInitHandler extends ChannelInitializer<NioSocketChannel> {
    private final ServiceGroup group;

    public ServiceInitHandler(ServiceGroup group) {
        this.group = group;
    }

    @Override
    protected void initChannel(NioSocketChannel channel) {
        ServiceConnection connection = new ServiceConnection(group, channel);
        channel.pipeline()
                .addLast(new LengthFieldPrepender(4))
                .addLast(new LengthFieldBasedFrameDecoder(0xffff, 0, 4, 0, 4))
                .addLast(new StringDecoder(StandardCharsets.UTF_8))
                .addLast(new StringEncoder(StandardCharsets.UTF_8))
                .addLast(new ServiceReadHandler(group, connection))
        ;
        ProxyServer.getInstance().getPluginManager().callEvent(new ServiceClientConnectedEvent(group, connection));
    }
}
