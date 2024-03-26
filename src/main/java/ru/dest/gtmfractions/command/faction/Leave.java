package ru.dest.gtmfractions.command.faction;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.dest.gtmfractions.GTMFactions;
import ru.dest.gtmfractions.command.annotation.FactionMember;
import ru.dest.library.command.ExecutionData;

@FactionMember
public class Leave extends FactionSub{
    public Leave(GTMFactions plugin) {
        super(plugin, "leave");
    }
    @Override
    public void perform(@NotNull ExecutionData data) throws Exception {
        Player player = data.executor();

        if(!plugin.getManager().isInFaction(player)){
            plugin.getLang().getMessage("error.not-in-faction").send(player);
            return;
        }

        plugin.getManager().leaveFaction(player);
    }
}
