package me.fixeddev.troll.troll.types;

import me.fixeddev.troll.troll.TrollType;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
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

        Minecart minecart = location.getWorld().spawn(location, Minecart.class);
        minecart.addPassenger(trolled);

        minecart.setVelocity(new Vector(0,15,0));

        Bukkit.getScheduler().runTaskLater(plugin, minecart::remove, 60);
    }

}
