package ru.dest.gtmfractions.command;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.dest.gtmfractions.GTMFactions;
import ru.dest.gtmfractions.command.faction.*;
import ru.dest.gtmfractions.storage.MemoryStore;
import ru.dest.library.command.CommandManager;
import ru.dest.library.command.ExecutionData;
import ru.dest.library.command.annotation.PlayerOnly;

public class FactionCommand extends CommandManager<GTMFactions> {

    public FactionCommand(GTMFactions plugin) {
        super(plugin, "faction", "Faction manage command", "/faction help", "f");

        addSubCommand(new Dismissal(plugin));
        addSubCommand(new Leave(plugin));
        addSubCommand(new Open(plugin));
        addSubCommand(new SetRank(plugin));
        addSubCommand(new SetLeader(plugin));
        addSubCommand(new Invite(plugin));
    }

    @Override
    protected void __default(@NotNull ExecutionData data) {
        plugin.getLang().getMessage("help").send(data.executor());
    }

    @PlayerOnly
    public void accept(@NotNull ExecutionData data){
        Player p = data.executor();

        if(!MemoryStore.INVITES.containsKey(p)){
            plugin.getLang().getMessage("error.not-invited").send(p);
            return;
        }
        String faction = MemoryStore.INVITES.get(p);

        plugin.getManager().addToFaction(p, faction);
    }

}
