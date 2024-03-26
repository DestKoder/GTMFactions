package ru.dest.gtmfractions.storage;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import ru.dest.gtmfractions.GTMFactions;
import ru.dest.gtmfractions.object.Faction;
import ru.dest.gtmfractions.object.FactionGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class FactionStorage {

    private final Map<String, Faction> factions = new HashMap<>();

    public Set<String> getFactionIds(){
        return factions.keySet();
    }

    public Faction getFaction(String key){
        return factions.get(key);
    }

    public FactionStorage(GTMFactions plugin, @NotNull ConfigurationSection factions){
        for(String factionId : factions.getKeys(false)){
            ConfigurationSection faction = factions.getConfigurationSection(factionId);
            if(faction == null) {
                plugin.logger().warning("Error loading faction " + factionId + ". Invalid structure");
                continue;
            }
            Map<String, FactionGroup> groups = new HashMap<>();

            for(String s : faction.getKeys(false)){
                FactionGroup group = getFactionGroup(s, Objects.requireNonNull(faction.getConfigurationSection(s)));
                groups.put(group.getId(), group);
            }

            this.factions.put(factionId, new Faction(factionId, groups));
        }
    }

    private @NotNull FactionGroup getFactionGroup(String id, @NotNull ConfigurationSection group){
        String permsGroup = group.getString("group");
        int salaryTime = group.getInt("salary-time");
        int salary = group.getInt("salary");

        String next = group.isSet("next") ? group.getString("next") : null;
        String prev = group.isSet("prev") ? group.getString("prev") : null;

        return new FactionGroup(id, permsGroup, salaryTime, salary, next, prev);
    }
}
