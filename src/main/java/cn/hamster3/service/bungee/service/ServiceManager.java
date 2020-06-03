package cn.hamster3.service.bungee.service;

import java.util.HashSet;

/**
 * 用于管理BungeeCord端的所有服务组
 */
public abstract class ServiceManager {
    private static final HashSet<ServiceGroup> groups = new HashSet<>();

    public static HashSet<ServiceGroup> getGroups() {
        return new HashSet<>(groups);
    }

    public static ServiceGroup getGroup(String name) {
        for (ServiceGroup group : groups) {
            if (group.getName().equalsIgnoreCase(name)) {
                return group;
            }
        }
        return null;
    }

    public static ServiceConnection getConnection(String name) {
        for (ServiceGroup group : groups) {
            for (ServiceConnection connection : group.getConnections()) {
                if (!connection.isRegistered()) {
                    continue;
                }
                if (connection.getName().equalsIgnoreCase(name)) {
                    return connection;
                }
            }
        }
        return null;
    }

    public static void addGroup(ServiceGroup group) {
        groups.add(group);
    }

    public static void removeGroup(ServiceGroup group) {
        groups.remove(group);
    }
}
