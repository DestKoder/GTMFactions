package ru.dest.gtmfractions.command.faction;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.dest.gtmfractions.GTMFactions;
import ru.dest.library.command.ExecutionData;
import ru.dest.library.command.annotation.exp.Arguments;
import ru.dest.library.command.annotation.exp.MultiPermission;

import java.util.ArrayList;
import java.util.List;

@MultiPermission({"factions.admin", "factions.setleader"})
@Arguments("{string} {player}")
public class SetLeader extends FactionSub{

    public SetLeader(GTMFactions plugin) {
        super(plugin, "setLeader");
    }

    @Override
    public void perform(@NotNull ExecutionData data) throws Exception {
        String faction = data.argument(0);
        Player player = data.getPlayer(1);

        plugin.getManager().setLeader(player, faction, data.executor());
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        if(args.length == 1){
            return new ArrayList<>(plugin.getFactions().getFactionIds());
        }
        if(args.length == 2){
            List<String> l = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(p -> l.add(p.getName()));
            return l;
        }

        return super.tabComplete(sender,alias,args);
    }
}
