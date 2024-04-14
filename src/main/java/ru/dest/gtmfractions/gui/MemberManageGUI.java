package ru.dest.gtmfractions.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import ru.dest.gtmfractions.GTMFactions;
import ru.dest.gtmfractions.storage.UserDataStorage;
import ru.dest.library.gui.GUIConfig;
import ru.dest.library.gui.ObjectListGUI;
import ru.dest.library.object.Pair;
import ru.dest.library.session.SessionManager;

import java.util.List;

public class MemberManageGUI extends ObjectListGUI<UserDataStorage.UserData> {

    public MemberManageGUI(GUIConfig template, Player opener, List<Pair<String, String>> titleFormat, @NotNull List<UserDataStorage.UserData> items) {
        super(template, opener, titleFormat, items);
        this.setOnItemClickHandler(this::onAction);
    }

    public void onAction(@NotNull InventoryClickEvent event, UserDataStorage.UserData obj){
        if(event.getClick() == ClickType.LEFT){
            //ЛКМ - понижаем / увольняем
            SessionManager.get().startSession(opener, GTMFactions.getInstance().getLang().getMessage("demote.question"), (pl, message) -> {
                if(message.equalsIgnoreCase("cancel")) return;
                GTMFactions.getInstance().getManager().demote(obj, pl, message);
            });
        }

        if(event.getClick() == ClickType.RIGHT){
            //ПКМ - повышаем?
            SessionManager.get().startSession(opener, GTMFactions.getInstance().getLang().getMessage("promote.question"), (pl, message) -> {
                if(message.equalsIgnoreCase("cancel")) return;
                GTMFactions.getInstance().getManager().promote(obj, pl, message);
            });
        }
    }



}
