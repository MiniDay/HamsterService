package cn.hamster3.service.bungee.command;

import cn.hamster3.service.bungee.service.ServiceGroup;
import cn.hamster3.service.bungee.service.ServiceManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class BroadcastCommand extends CommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        TextComponent component = new TextComponent();
        component.setColor(ChatColor.RED);
        if (args.length < 2) {
            component.setText("请输入服务组名称!");
            sender.sendMessage(component);
            return;
        }
        ServiceGroup group = ServiceManager.getGroup(args[1]);
        if (group == null) {
            component.setText("未找到服务组 " + args[1] + " !");
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
        group.broadcast(builder.toString());
        component.setText("消息广播成功!");
        component.setColor(ChatColor.GREEN);
        sender.sendMessage(component);
    }
}
