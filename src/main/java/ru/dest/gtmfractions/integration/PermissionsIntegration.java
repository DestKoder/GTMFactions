package ru.dest.gtmfractions.integration;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class PermissionsIntegration {

    private final Permission permission;

    private PermissionsIntegration(Permission permission){
        this.permission = permission;
    }

    public static @Nullable PermissionsIntegration hook(@NotNull Server server){
        if(!server.getPluginManager().isPluginEnabled("Vault")) return null;

        RegisteredServiceProvider<Permission> provider = server.getServicesManager().getRegistration(Permission.class);

        if(provider == null) return null;

        return new PermissionsIntegration(provider.getProvider());
    }

    public void addPlayerToGroup(OfflinePlayer player, String group){
        permission.playerAddGroup("world",player, group);
    }

    public void removePlayerFromGroup(OfflinePlayer player,String group){
        permission.playerRemoveGroup("world",player, group);
    }

    public boolean hasGroup(Player player, @NotNull String group){
        return Arrays.stream(permission.getPlayerGroups(player)).anyMatch(group::equalsIgnoreCase);
    }
}
