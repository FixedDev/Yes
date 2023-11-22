package me.fixeddev.troll;

import me.fixeddev.troll.commands.TrollCommand;
import me.fixeddev.troll.listeners.UserListeners;
import me.fixeddev.troll.menu.TrollUserMenu;
import me.fixeddev.troll.troll.SimpleTrollTypesRegistry;
import me.fixeddev.troll.troll.TrollTypesRegistry;
import me.fixeddev.troll.troll.types.FakeCreepersTroll;
import me.fixeddev.troll.troll.types.KnockbackZombieTroll;
import me.fixeddev.troll.troll.types.SlingshotTroll;
import me.fixeddev.troll.troll.types.magiccarpet.MagicCarpetTroll;
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
        trollTypesRegistry.register(new KnockbackZombieTroll(this));
        trollTypesRegistry.register(new FakeCreepersTroll(this));
        trollTypesRegistry.register(new SlingshotTroll(this));
        trollTypesRegistry.register(new MagicCarpetTroll(this));

        userRegistry = new YamlUserRegistry(new File(getDataFolder(), "users"), trollTypesRegistry);

        getServer().getScheduler().runTaskAsynchronously(this, () -> { // let's run it on another thread.
            userRegistry.load();
        });

        getServer().getPluginManager().registerEvents(new UserListeners(userRegistry), this);

        TrollUserMenu trollUserMenu = new TrollUserMenu(trollTypesRegistry);

        getCommand("troll").setExecutor(new TrollCommand(trollUserMenu, userRegistry));
    }

    public TrollTypesRegistry getTrollTypesRegistry() {
        return trollTypesRegistry;
    }

    public UserRegistry getUserRegistry() {
        return userRegistry;
    }
}
