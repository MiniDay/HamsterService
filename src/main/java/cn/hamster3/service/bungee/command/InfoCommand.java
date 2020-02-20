package cn.hamster3.service.bungee.command;

import cn.hamster3.service.bungee.service.ServiceConnection;
import cn.hamster3.service.bungee.service.ServiceGroup;
import cn.hamster3.service.bungee.service.ServiceManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashSet;

public class InfoCommand extends CommandExecutor {
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
        component.setColor(ChatColor.YELLOW);
        component.setText("=============== 服务组信息 ===============");
        sender.sendMessage(component);

        component.setColor(ChatColor.GREEN);
        component.setText("服务组名称: " + group.getName());
        sender.sendMessage(component);
        component.setText("服务组地址: " + group.getHost());
        sender.sendMessage(component);
        component.setText("服务组端口: " + group.getPort());
        sender.sendMessage(component);

        component.setColor(ChatColor.YELLOW);
        component.setText("=============== 服务组连接 ===============");
        sender.sendMessage(component);

        HashSet<ServiceConnection> connections = group.getConnections();
        for (ServiceConnection connection : connections) {
            if (connection.isRegistered()) {
                component.setColor(ChatColor.GREEN);
                component.setText(String.format("服务器名称: [%s] [%s]", connection.getName(), "已注册"));
                sender.sendMessage(component);

                component.setText(String.format("  连接地址: [%s:%d]",
                        connection.getConnectionAddress().getHostString(),
                        connection.getConnectionAddress().getPort()));
                sender.sendMessage(component);

                component.setText(String.format("  服务器地址: [%s:%d]",
                        connection.getBukkitAddress(),
                        connection.getBukkitPort()));
            } else {
                component.setColor(ChatColor.RED);
                component.setText(String.format("服务器名称: [%s] [%s]", connection.getName(), "未注册"));
                sender.sendMessage(component);

                component.setText(String.format("  连接地址: [%s:%d]",
                        connection.getConnectionAddress().getHostString(),
                        connection.getConnectionAddress().getPort()));
            }
            sender.sendMessage(component);
        }
        if (connections.isEmpty()) {
            component.setText("[无]");
            component.setColor(ChatColor.BLUE);
            sender.sendMessage(component);
        }
    }


}
