package cn.hamster3.service.bungee.command;

import cn.hamster3.service.bungee.service.ServiceGroup;
import cn.hamster3.service.bungee.service.ServiceManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashSet;

public class ListCommand extends CommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        TextComponent component = new TextComponent();
        component.setColor(ChatColor.RED);
        component.setColor(ChatColor.YELLOW);
        component.setText("=============== 服务组列表 ===============");
        sender.sendMessage(component);

        HashSet<ServiceGroup> groups = ServiceManager.getGroups();

        component.setColor(ChatColor.GREEN);
        for (ServiceGroup group : groups) {
            component.setText(String.format("[%s] 本地地址: %s:%d, 服务连接数: %d.",
                    group.getName(),
                    group.getHost(),
                    group.getPort(),
                    group.getConnections().size()
            ));
            sender.sendMessage(component);
        }

        if (groups.isEmpty()) {
            component.setText("[无]");
            component.setColor(ChatColor.BLUE);
            sender.sendMessage(component);
        }
    }


}
