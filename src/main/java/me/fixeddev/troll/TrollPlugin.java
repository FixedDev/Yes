package me.fixeddev.troll;

import me.fixeddev.troll.listeners.UserListeners;
import me.fixeddev.troll.troll.SimpleTrollTypesRegistry;
import me.fixeddev.troll.troll.TrollTypesRegistry;
import me.fixeddev.troll.user.UserRegistry;
import me.fixeddev.troll.user.YamlUserRegistry;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class TrollPlugin extends JavaPlugin {

    private TrollTypesRegistry trollTypesRegistry;
    private UserRegistry userRegistry;

    @Override
    public void onDisable() {
        if (userRegistry != null) {
            userRegistry.save();
        }
    }

    @Override
    public void onEnable() {
        trollTypesRegistry = new SimpleTrollTypesRegistry();
        userRegistry = new YamlUserRegistry(new File(getDataFolder(), "users"), trollTypesRegistry);

        getServer().getScheduler().runTaskAsynchronously(this, () -> { // let's run it on another thread.
            userRegistry.load();
        });

        getServer().getPluginManager().registerEvents(new UserListeners(userRegistry), this);
    }

    public TrollTypesRegistry getTrollTypesRegistry() {
        return trollTypesRegistry;
    }

    public UserRegistry getUserRegistry() {
        return userRegistry;
    }
}
