package ru.dest.gtmfractions.command.faction;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.entity.Player;
import ru.dest.gtmfractions.GTMFactions;
import ru.dest.gtmfractions.command.annotation.FactionMember;
import ru.dest.gtmfractions.object.Faction;
import ru.dest.library.command.ExecutionData;
import ru.dest.library.command.annotation.exp.Arguments;
import ru.dest.library.command.annotation.exp.MultiPermission;
import ru.dest.library.object.lang.IAdvancedMessage;

@MultiPermission("factions.invite")
@Arguments("{player}")
@FactionMember
public class Invite extends FactionSub{

    public Invite(GTMFactions plugin) {
        super(plugin, "invite");
    }

    @Override
    public void perform(ExecutionData data) throws Exception {
        Player actioned = data.getPlayer(0);
        Faction faction = plugin.getManager().getPlayerFaction(data.executor());

        if(faction == null){
            plugin.getLang().getMessage("error.not-in-faction").send(data.executor());
            return;
        }

        if(plugin.getManager().isInFaction(actioned)){
            plugin.getLang().getMessage("error.in-faction").send(data.executor());
            return;
        }

        IAdvancedMessage accept = plugin.getLang().getMessage("buttons.accept").modify();
        IAdvancedMessage deny = plugin.getLang().getMessage("buttons.deny").modify();

        accept.addClickHandler(ClickEvent.Action.RUN_COMMAND, "faction accept");
        deny.addClickHandler(ClickEvent.Action.RUN_COMMAND, "faction deny");

        IAdvancedMessage msg = plugin.getLang().getMessage("success.invite.user")
                .format("player", data.executor().getName())
                .format("faction", faction.getId())
                .modify().add(accept).add(deny);

        msg.send(actioned, ChatMessageType.CHAT);
        plugin.getLang().getMessage("success.invite.admin").format("player", actioned.getName()).send(data.executor());
    }
}
