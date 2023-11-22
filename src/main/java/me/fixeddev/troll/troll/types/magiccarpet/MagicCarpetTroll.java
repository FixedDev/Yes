package me.fixeddev.troll.troll.types.magiccarpet;

import me.fixeddev.troll.troll.TrollType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Random;

public class MagicCarpetTroll implements TrollType {

    private final Plugin plugin;
    private final MagicCarpetHandler magicCarpetHandler;

    public MagicCarpetTroll(Plugin plugin) {
        this.plugin = plugin;
        magicCarpetHandler = new MagicCarpetHandler(plugin);
    }

    @Override
    public String id() {
        return "magic-carpet";
    }

    @Override
    public Component displayName() {
        return Component.translatable("troll.types.magic-carpet");
    }

    @Override
    public void executeTroll(Player troll, Player trolled) {
        Location location = trolled.getLocation().toHighestLocation().add(0, 50, 0);

        trolled.teleport(location);
        magicCarpetHandler.startMagicCarpet(trolled);
        trolled.sendMessage(Component.translatable("troll.magic-carpet.start"));

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            magicCarpetHandler.stopMagicCarpet(trolled);
            trolled.sendMessage(Component.translatable("troll.magic-carpet.bye-bye"));
        }, 80);
    }

}