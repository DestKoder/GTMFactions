package ru.dest.gtmfractions.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import ru.dest.library.storage.DataObject;
import ru.dest.library.storage.OneFileStorage;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class UserDataStorage extends OneFileStorage<UUID, UserDataStorage.UserData> {

    public UserDataStorage(@NotNull File data) throws IOException {
        super(data);
    }

    public void remove(UUID uuid){
        data.remove(uuid);
    }

    @Override
    public void load() throws Exception {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(dataFile);
        for(String s : cfg.getKeys(false)){
            UUID uuid = UUID.fromString(s);
            String faction = cfg.getString(s + ".faction");
            String group = cfg.getString(s + ".group");

            UserData dat = new UserData(faction, group, uuid);
            data.put(dat.getKey(), dat);
        }
    }

    @Override
    public void save() throws Exception {
        FileConfiguration cfg = new YamlConfiguration();

        data.forEach((uuid, userData) -> {
            cfg.set(uuid.toString()+".faction", userData.getFaction());
            cfg.set(uuid.toString()+".group", userData.getGroup());
        });

        cfg.save(dataFile);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class  UserData implements DataObject<UUID> {
        private final String faction;
        private final String group;
        private final UUID key;
    }
}
