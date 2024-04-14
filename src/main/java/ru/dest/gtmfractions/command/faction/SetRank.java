package ru.dest.gtmfractions.command.faction;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.dest.gtmfractions.GTMFactions;
import ru.dest.gtmfractions.object.Faction;
import ru.dest.gtmfractions.object.FactionGroup;
import ru.dest.library.command.ExecutionData;
import ru.dest.library.command.annotation.Permission;
import ru.dest.library.command.annotation.RequireArgs;
import ru.dest.library.command.annotation.exp.MultiPermission;

import java.util.ArrayList;
import java.util.List;

@MultiPermission({"factions.setrank", "factions.admin"})
@RequireArgs(args = "{string} {string} {player}")
public class SetRank extends FactionSub{

    public SetRank(GTMFactions plugin) {
        super(plugin, "setrank");
    }

    @Override
    public void perform(ExecutionData data) throws Exception {
        Faction f = plugin.getFactions().getFaction(data.argument(0));

        if(f == null){
            plugin.getLang().getMessage("error.faction-not-found").send(data.executor());
            return;
        }
        FactionGroup group = f.getGroup(data.argument(1));

        if(group == null){
            plugin.getLang().getMessage("error.group-not-found").send(data.executor());
            return;
        }
        Player player = data.getPlayer(2);

        plugin.getManager().setRank(player, f, group, data.executor());
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
//        List<String> result = new ArrayList<>();
        if(args.length == 1){
            return new ArrayList<>(plugin.getFactions().getFactionIds());
        }

        if(args.length == 2) {
            Faction faction = plugin.getFactions().getFaction(args[0]);
            if(faction == null) return new ArrayList<>();
            return new ArrayList<>(faction.getGroups());
        }

        if(args.length == 3){
            List<String> l = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(p -> l.add(p.getName()));
            return l;
        }

        return super.tabComplete(sender,alias,args);
    }
}
