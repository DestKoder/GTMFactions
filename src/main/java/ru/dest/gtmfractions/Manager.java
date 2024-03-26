package ru.dest.gtmfractions;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.dest.gtmfractions.object.Faction;
import ru.dest.gtmfractions.object.FactionGroup;
import ru.dest.gtmfractions.storage.UserDataStorage;

public class Manager {

    private final GTMFactions plugin;
    public Manager(GTMFactions plugin) {
        this.plugin = plugin;
    }

    public boolean isInFaction(@NotNull Player player){
        return plugin.getUsers().has(player.getUniqueId());
    }

    public Faction getPlayerFaction(Player player){
        if(!isInFaction(player)) return null;
        return plugin.getFactions().getFaction(plugin.getUsers().get(player.getUniqueId()).getFaction());
    }

    public void setLeader(@NotNull Player player, String faction, CommandSender admin){
        UserDataStorage.UserData data = plugin.getUsers().get(player.getUniqueId());
        Faction f = plugin.getFactions().getFaction(faction);

        if(f == null) {
            plugin.getLang().getMessage("error.faction-not-found").send(admin);
            return;
        }


        if(data == null || data.getFaction().equals(faction)){
            data = new UserDataStorage.UserData(faction, "leader", player.getUniqueId());
            plugin.getUsers().add(data);

            plugin.getPermissionWorker().addPlayerToGroup(player, f.getGroup("leader").getGroup());

            sendSetLeaderMessages(player, admin, faction);
            return;
        }

        leaveFaction(player);
        data = new UserDataStorage.UserData(faction, "leader", player.getUniqueId());
        plugin.getUsers().add(data);

        plugin.getPermissionWorker().addPlayerToGroup(player, f.getGroup("leader").getGroup());

        sendSetLeaderMessages(player, admin, faction);
    }

    private void sendSetLeaderMessages(Player player, CommandSender admin, String faction){
        plugin.getLang().getMessage("success.setLeader.admin").format("player", player.getName())
                .format("faction", faction).send(admin);
        plugin.getLang().getMessage("success.setLeader.user").format("player", admin.getName())
                .format("faction", faction).send(player);
    }

    public void setRank(@NotNull Player player, Faction faction, FactionGroup rank, CommandSender admin){
        UserDataStorage.UserData data = plugin.getUsers().get(player.getUniqueId());

        if(data != null && data.getGroup().equals("leader")){
            plugin.getLang().getMessage("error.setrank-leader").send(admin);
            return;
        }

        if(data == null || data.getFaction().equals(faction.getId())){

            data = new UserDataStorage.UserData(faction.getId(), rank.getId(), player.getUniqueId());
            plugin.getUsers().add(data);

            plugin.getPermissionWorker().addPlayerToGroup(player, rank.getGroup());

            sendSetRankMessages(player, admin, faction.getId());
            return;
        }

        leaveFaction(player);
        data = new UserDataStorage.UserData(faction.getId(), rank.getId(), player.getUniqueId());
        plugin.getUsers().add(data);

        plugin.getPermissionWorker().addPlayerToGroup(player, rank.getGroup());

        sendSetRankMessages(player, admin, faction.getId());
    }

    private void sendSetRankMessages(@NotNull Player player, CommandSender admin, String faction){
        plugin.getLang().getMessage("success.setRank.admin").format("player", player.getName())
                .format("faction", faction).send(admin);
    }

    public void leaveFaction(@NotNull Player player){
        Faction f = plugin.getFactions().getFaction(plugin.getUsers().get(player.getUniqueId()).getFaction());

        for(FactionGroup group : f.listGroups()){
            plugin.getPermissionWorker().removePlayerFromGroup(player, group.getGroup());
            return;
        }

        plugin.getUsers().remove(player.getUniqueId());

        plugin.getLang().getMessage("success.leave").format("faction", f.getId()).send(player);
    }

    public void addToFaction(Player player, String faction){
        if(isInFaction(player)) return;

        Faction f = plugin.getFactions().getFaction(faction);
        if(f == null) {
            plugin.getLogger().warning("Trying to add player to faction which is not exists");
            plugin.getLang().getMessage("error.internal").send(player);
            return;
        }

        FactionGroup group = f.getFirstGroup();

        if(group == null){
            plugin.getLogger().warning("Couldn't find first group for faction " + faction +". Is it configured correctly");
            plugin.getLang().getMessage("error.internal").send(player);
            return;
        }

        plugin.getUsers().add(new UserDataStorage.UserData(f.getId(), group.getId(), player.getUniqueId()));
        plugin.getPermissionWorker().addPlayerToGroup(player, group.getGroup());
        plugin.getLang().getMessage("success.invite.accept").format("faction", f.getId()).send(player);
    }

}
