package ru.dest.gtmfractions.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import ru.dest.gtmfractions.GTMFactions;
import ru.dest.library.object.BukkitItem;
import ru.dest.library.object.Pair;
import ru.dest.library.storage.DataObject;
import ru.dest.library.storage.OneFileStorage;
import ru.dest.library.utils.ColorUtils;
import ru.dest.library.utils.StringUtils;
import ru.dest.library.utils.TimeUtils;
import ru.dest.library.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    public List<UserData> getFactionMembers(String faction){
        List<UserData> result = new ArrayList<>();

        for(UserData d : data.values()){
            if(d.getFaction().equals(faction) && !d.getGroup().equals("leader")) result.add(d);
        }
        return result;
    }

    public List<UserData> getFactionUsers(String faction){
        List<UserData> result = new ArrayList<>();

        for(UserData d : data.values()){
            if(d.getFaction().equals(faction)) result.add(d);
        }
        return result;
    }

    public UserData getFactionLeader(String faction){
        for(UserData d : data.values()){
            if(d.getFaction().equals(faction) && d.getGroup().equals("leader")) return d;
        }
        return null;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class  UserData implements DataObject<UUID>, BukkitItem {
        private final long addTime = TimeUtils.getCurrentUnixTime();
        private final String faction;
        private final String group;
        private final UUID key;

        @Override
        public ItemStack getItem() {
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) item.getItemMeta();

            OfflinePlayer player = Bukkit.getOfflinePlayer(key);

            assert meta != null;
            meta.setOwningPlayer(player);
            meta.setDisplayName(player.getName());
            List<String> lore = new ArrayList<>();

            for(String s : GTMFactions.getInstance().getConfig().getStringList("member-lore")){
                lore.add(ColorUtils.parse(StringUtils.format(s, Utils.newList(
                        new Pair<>("date", TimeUtils.formatUnixTime(addTime, TimeUtils.DEFAULT_TIME_FORMAT.DD_MM_YYYY_POINTS)),
                        new Pair<>("online", player.isOnline() ?
                                GTMFactions.getInstance().getLang().getValue("member-online")
                                :
                                TimeUtils.formatTimeInMillis(player.getLastPlayed(), TimeUtils.DEFAULT_TIME_FORMAT.FULL_POINTS)),
                        new Pair<>("rank", group)
                ))));
            }

            meta.setLore(lore);
            item.setItemMeta(meta);
            return item;
        }
    }
}
