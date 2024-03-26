package ru.dest.gtmfractions.command.faction;

import ru.dest.gtmfractions.GTMFactions;
import ru.dest.library.command.BukkitCommand;

public abstract class FactionSub extends BukkitCommand<GTMFactions> {

    public FactionSub(GTMFactions plugin, String name) {
        super(plugin, name);
    }

}
