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
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FakeCreepersTroll implements TrollType {

    private final Plugin plugin;
    private final Random random = new Random();

    public FakeCreepersTroll(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String id() {
        return "fake-creepers";
    }

    @Override
    public Component displayName() {
        return Component.translatable("troll.type.fake-creepers");
    }

    @Override
    public void executeTroll(Player troll, Player trolled) {
        Location location = trolled.getEyeLocation().clone();
        location.setY(trolled.getLocation().getY());

        List<Creeper> creeperList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Creeper creeper = generateCreeper(location);
            creeperList.add(creeper);
            spawnEntity(trolled, creeper);
        }

        trolled.playSound(location, Sound.ENTITY_CREEPER_PRIMED, 1, 0);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Creeper creeper : creeperList) {
                removeEntity(trolled, creeper);
            }

            trolled.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1, 0);
        }, 35);
    }

    @NotNull
    private Creeper generateCreeper(Location location) {
        World world = location.getWorld();
        Level nmsWorld = ((CraftWorld) world).getHandle();

        Creeper creeper = new Creeper(EntityType.CREEPER, nmsWorld);
        creeper.setIgnited(true);
        creeper.setInvulnerable(true);
        creeper.setPowered(true);
        creeper.setSwellDir(2);
        creeper.teleportTo(location.getX() + random.nextDouble(-5, 5), location.getY(), location.getZ() + random.nextDouble(-5, 5));
        creeper.lookAt(EntityAnchorArgument.Anchor.FEET, new Vec3(location.getX(), location.getY(), location.getZ()));

        return creeper;
    }

    private void spawnEntity(Player player, Creeper creeper) {
        ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(creeper);
        sendPacket(player, addEntityPacket);

        creeper.getEntityData().refresh(((CraftPlayer) player).getHandle());
    }

    private void removeEntity(Player player, Creeper creeper) {
        ClientboundRemoveEntitiesPacket addEntityPacket = new ClientboundRemoveEntitiesPacket(creeper.getId());
        sendPacket(player, addEntityPacket);
    }

    private void sendPacket(Player player, Packet<?> packet) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().connection.send(packet);
    }
}
