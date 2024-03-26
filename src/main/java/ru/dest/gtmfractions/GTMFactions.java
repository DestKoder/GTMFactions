package ru.dest.gtmfractions;

import lombok.Getter;
import org.bukkit.entity.Player;
import ru.dest.gtmfractions.command.FactionCommand;
import ru.dest.gtmfractions.command.annotation.FactionMember;
import ru.dest.gtmfractions.integration.PermissionsIntegration;
import ru.dest.gtmfractions.storage.FactionStorage;
import ru.dest.gtmfractions.storage.UserDataStorage;
import ru.dest.library.bukkit.BukkitPlugin;
import ru.dest.library.helpers.AnnotationValidator;
import ru.dest.library.helpers.ArgumentValidator;
import ru.dest.library.locale.Lang;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
@Getter
public final class GTMFactions extends BukkitPlugin<GTMFactions> {

    private Lang lang;
    private FactionStorage factions;
    private UserDataStorage users;
    private Manager manager;

    private PermissionsIntegration permissionWorker;
    @Override
    public void load() throws IOException {
        File config = new File(getDataFolder(), "config.yml");

        if(!config.exists()){
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }

        File lang = new File(getDataFolder(), "lang");
        if(!lang.exists()) lang.mkdirs();

        saveIfNotExists("lang/ru.yml");
        File userData = new File(getDataFolder(), "userdata.yml");
        if(!userData.exists()){
            userData.createNewFile();
        }
    }

    @Override
    public void enable() throws IOException {
        this.lang = loadLang(getConfig().getString("settings.lang") + ".yml");
        this.factions = new FactionStorage(this, Objects.requireNonNull(getConfig().getConfigurationSection("factions")));
        this.users = new UserDataStorage(new File(getDataFolder(), "userdata.yml"));

        this.permissionWorker = PermissionsIntegration.hook(getServer());

        if(permissionWorker == null){
            logger.warning("Couldn't initialize permission worker. Is Vault & Permission plugin installed???");
            pluginManager.disablePlugin(this);
            return;
        }

        manager = new Manager(this);

        AnnotationValidator.regChecker(FactionMember.class, (data, ann) ->{
            if(!(data.executor() instanceof Player)) return false;

            return users.has(((Player) data.executor()).getUniqueId());
        });;

        registry.register(new FactionCommand(this));
    }

    @Override
    public void disable() throws Exception {
        users.save();
    }
}
