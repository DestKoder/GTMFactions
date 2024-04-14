package ru.dest.gtmfractions.command.faction;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.dest.gtmfractions.GTMFactions;
import ru.dest.gtmfractions.command.annotation.FactionMember;
import ru.dest.gtmfractions.gui.MemberManageGUI;
import ru.dest.gtmfractions.object.Faction;
import ru.dest.gtmfractions.storage.UserDataStorage;
import ru.dest.library.Library;
import ru.dest.library.command.ExecutionData;
import ru.dest.library.command.annotation.exp.MultiPermission;
import ru.dest.library.gui.GUI;
import ru.dest.library.object.Pair;
import ru.dest.library.utils.PlayerUtils;
import ru.dest.library.utils.Utils;

import java.util.List;

@MultiPermission("factions.open")
@FactionMember
public class Open extends FactionSub{

    public Open(GTMFactions plugin) {
        super(plugin, "open");
    }

    @Override
    public void perform(@NotNull ExecutionData data) throws Exception {
        Player p = data.executor();
        UserDataStorage.UserData user = plugin.getUsers().get(p.getUniqueId());

        List<UserDataStorage.UserData> members = plugin.getUsers().getFactionMembers(user.getFaction());
        UserDataStorage.UserData leader = plugin.getUsers().getFactionLeader(user.getFaction());

        PlayerUtils.openGUI(
                new MemberManageGUI(plugin.getGuiContainer().get("member-manage"), p, Utils.newList(
                        new Pair<>("faction", user.getFaction()),
                        new Pair<>("members", members.size()+""),
                        new Pair<>("leader", Bukkit.getOfflinePlayer(leader.getKey()).getName())
                ), members)
        );
    }
}
