package cn.hamster3.service.bungee.command;

import cn.hamster3.service.bungee.service.ServiceGroup;
import cn.hamster3.service.bungee.service.ServiceManager;
import io.netty.util.concurrent.Future;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

public class CloseCommand extends ServiceCommandExecutor {
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
        Future<?> f = group.close();
        f.addListener(future -> {
            if (future.isSuccess()) {
                component.setText(String.format("服务组 %s 在 %s:%d 上关闭成功!", group.getName(), group.getHost(), group.getPort()));
                component.setColor(ChatColor.GREEN);
            } else {
                component.setText(String.format("服务组 %s 在 %s:%d 上关闭时出现异常: %s", group.getName(), group.getHost(), group.getPort(), future.cause()));
            }
            sender.sendMessage(component);
        });
        ServiceManager.removeGroup(group);
    }

}
