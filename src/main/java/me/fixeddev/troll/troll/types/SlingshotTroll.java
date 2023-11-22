package me.fixeddev.troll.troll.types;

import me.fixeddev.troll.Translator;
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
    private final Translator translator;

    public SlingshotTroll(Plugin plugin, Translator translator) {
        this.plugin = plugin;
        this.translator = translator;
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
        translator.send(trolled, "troll.slingshot.bye-bye");

        Minecart minecart = location.getWorld().spawn(location, Minecart.class);
        minecart.addPassenger(trolled);

        minecart.setVelocity(new Vector(0,15,0));

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            minecart.remove();
            translator.send(trolled, "troll.slingshot.good-luck");

        }, 60);
    }

}
