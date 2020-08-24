package cn.hamster3.service.bungee.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;

public class ServiceCommand extends Command {
    private final HashMap<String, ServiceCommandExecutor> commands;

    public ServiceCommand() {
        super("HamsterService", "service.admin", "service");
        commands = new HashMap<>();
        commands.put("info", new InfoCommand());
        commands.put("list", new ListCommand());
        commands.put("close", new CloseCommand());
        commands.put("message", new MessageCommand());
        commands.put("command", new DispatchCommand());
        commands.put("broadcast", new BroadcastCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("service.admin")) {
            TextComponent component = new TextComponent("你没有使用这个命令的权限!");
            component.setColor(ChatColor.RED);
            sender.sendMessage(component);
            return;
        }
        if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
            return;
        }
        ServiceCommandExecutor executor = commands.get(args[0]);
        if (executor == null) {
            TextComponent component = new TextComponent("未找到该命令! 请输入 /service help 以查看命令帮助!");
            component.setColor(ChatColor.RED);
            return;
        }
        executor.execute(sender, args);
    }

    private void sendHelp(CommandSender sender) {
        TextComponent component = new TextComponent();
        component.setColor(ChatColor.GREEN);
        component.setText("/service list");
        sender.sendMessage(component);
        component.setColor(ChatColor.YELLOW);
        component.setText("查看所有服务组列表");
        sender.sendMessage(component);

        component.setColor(ChatColor.GREEN);
        component.setText("/service info <服务组>");
        sender.sendMessage(component);
        component.setColor(ChatColor.YELLOW);
        component.setText("查看某个服务组的信息");
        sender.sendMessage(component);

        component.setColor(ChatColor.GREEN);
        component.setText("/service start <组名> <密码> <端口>");
        sender.sendMessage(component);
        component.setColor(ChatColor.YELLOW);
        component.setText("打开一个服务组");
        sender.sendMessage(component);

        component.setColor(ChatColor.GREEN);
        component.setText("/service close <组名>");
        sender.sendMessage(component);
        component.setColor(ChatColor.YELLOW);
        component.setText("关闭一个服务组");
        sender.sendMessage(component);

        component.setColor(ChatColor.GREEN);
        component.setText("/service message <子服名称> <消息>");
        sender.sendMessage(component);
        component.setColor(ChatColor.YELLOW);
        component.setText("单独给某个服务器发一条消息");
        sender.sendMessage(component);

        component.setColor(ChatColor.GREEN);
        component.setText("/service broadcast <服务组> <消息>");
        sender.sendMessage(component);
        component.setColor(ChatColor.YELLOW);
        component.setText("在一个服务组内广播一条消息");
        sender.sendMessage(component);
    }
}
