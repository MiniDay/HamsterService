package cn.hamster3.service.bungee.command;

import cn.hamster3.service.bungee.service.ServiceConnection;
import cn.hamster3.service.bungee.service.ServiceManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class MessageCommand extends ServiceCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        TextComponent component = new TextComponent();
        component.setColor(ChatColor.RED);
        if (args.length < 2) {
            component.setText("请输入子服名称!");
            sender.sendMessage(component);
            return;
        }
        ServiceConnection connection = ServiceManager.getConnection(args[1]);
        if (connection == null) {
            component.setText("未找到该子服, 可能是链接尚未注册!");
            sender.sendMessage(component);
            return;
        }
        if (args.length < 3) {
            component.setText("请输入要发送的消息!");
            sender.sendMessage(component);
            return;
        }
        StringBuilder builder = new StringBuilder(args[2]);
        for (int i = 3; i < args.length; i++) {
            builder.append(' ').append(args[i]);
        }
        connection.sendMessage(builder.toString());
        component.setText("消息发送成功!");
        component.setColor(ChatColor.GREEN);
        sender.sendMessage(component);
    }
}
