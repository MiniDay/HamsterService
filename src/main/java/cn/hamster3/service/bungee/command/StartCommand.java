package cn.hamster3.service.bungee.command;

import cn.hamster3.service.bungee.service.ServiceGroup;
import cn.hamster3.service.bungee.service.ServiceManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class StartCommand extends CommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        TextComponent component = new TextComponent();
        component.setColor(ChatColor.RED);
        if (args.length < 2) {
            component.setText("请输入服务组名称!");
            sender.sendMessage(component);
            return;
        }
        if (args.length < 3) {
            component.setText("请输入服务组密码!");
            sender.sendMessage(component);
            return;
        }
        if (args.length < 4) {
            component.setText("请输入服务组端口!");
            sender.sendMessage(component);
            return;
        }
        int port;
        try {
            port = Integer.parseInt(args[3]);
        } catch (Exception e) {
            component.setText("\"" + args[3] + "\" 不是一个有效的数字!");
            sender.sendMessage(component);
            return;
        }
        for (ServiceGroup group : ServiceManager.getGroups()) {
            if (group.getName().equalsIgnoreCase(args[1])) {
                component.setText("已经有一个服务组使用了 " + args[1] + " 这个名字!");
                sender.sendMessage(component);
                return;
            }
        }
        ServiceGroup group = new ServiceGroup(port, args[1], args[2]);
        group.start().addListener(future -> {
            if (future.isSuccess()) {
                component.setText("服务组 [" + args[1] + "] 在端口 " + port + " 上启动成功!");
                component.setColor(ChatColor.GREEN);
            } else {
                component.setText("服务组 [" + args[1] + "] 在端口 " + port + " 上启动失败: " + future.cause());
            }
            sender.sendMessage(component);
        });
    }
}
