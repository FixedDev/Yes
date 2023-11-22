package me.fixeddev.troll.troll.types;

import me.fixeddev.troll.troll.TrollType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Random;

public class SlingshotTroll implements TrollType {

    private final Plugin plugin;
    private final Random random = new Random();

    public SlingshotTroll(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String id() {
        return "slingshot";
    }

    @Override
    public Component displayName() {
        return Component.translatable("troll.type.slingshot");
    }

    @Override
    public void executeTroll(Player troll, Player trolled) {
        Location location = trolled.getLocation();
        trolled.sendMessage(Component.translatable("troll.slingshot.bye-bye"));

        Minecart minecart = location.getWorld().spawn(location, Minecart.class);
        minecart.addPassenger(trolled);

        minecart.setVelocity(new Vector(0,15,0));

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            minecart.remove();
            trolled.sendMessage(Component.translatable("troll.slingshot.good-luck"));

        }, 60);
    }

}
