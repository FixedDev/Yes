package me.fixeddev.troll.troll.types.magiccarpet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.block.data.CraftBlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class MagicCarpetHandler implements Listener {

    private final Set<UUID> magicCarpetPlayers = new HashSet<>();

    protected static final BlockData GLASS_DATA = Material.GLASS.createBlockData();
    protected static final BlockData AIR_DATA = Material.AIR.createBlockData();

    private final MagicCarpetListeners listener;
    private final Map<UUID, Material> lastBlockType;

    public MagicCarpetHandler(Plugin plugin) {
        listener = new MagicCarpetListeners(this);
        lastBlockType = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    public void startMagicCarpet(Player player) {
        Block block = player.getLocation().subtract(0, 1, 0).getBlock();

        if (block.getType() == Material.AIR) {
            setBlock(player, block, Material.GLASS);
            player.sendBlockChange(block.getLocation(), GLASS_DATA);
        }

        setPlatform(block.getLocation(), player);

        magicCarpetPlayers.add(player.getUniqueId());
    }

    public void stopMagicCarpet(Player player) {
        Block block = player.getLocation().subtract(0, 1, 0).getBlock();

        if (magicCarpetPlayers.remove(player.getUniqueId())) {
            resetLastBlock(player, block);
        }
    }

    public boolean hasMagicCarpet(Player player) {
        return magicCarpetPlayers.contains(player.getUniqueId());
    }

    Material popLast(Player player) {
        return lastBlockType.remove(player.getUniqueId());
    }

    void setPlatform(Location location, Player player) {
        Platform platform = generate(location, 1, 1);

        for (Location loc : platform.platformLocations()) {
            if (loc.getBlock().getType() != Material.AIR) {
                continue;
            }

            player.sendBlockChange(loc, GLASS_DATA);
        }
    }

    void resetLastBlock(Player player, Block block) {
        block.setType(lastBlockType.getOrDefault(player.getUniqueId(), Material.AIR));

        removePlatform(block.getLocation(), player);
    }

    void setBlock(Player player, Block block, Material newType) {
        setBlock(player, block, newType, false);
    }

    void setBlock(Player player, Block block, Material newType, boolean update) {
        lastBlockType.put(player.getUniqueId(), block.getType());

        if (update) {
            block.setType(newType);
        } else {
            setBlockWithoutUpdating(block, newType);
        }
    }

    void updatePlatform(Location fromLocation, Location toLocation, Player player) {
        List<Location> blocksNotLongerPart;
        List<Location> newBlocks;

        Platform old = generate(fromLocation, 1, 1);
        Platform newPlatform = generate(toLocation, 1, 1);

        blocksNotLongerPart = old.subtract(newPlatform);
        newBlocks = newPlatform.subtract(old);

        for (Location loc : newBlocks) {
            if (loc.getBlock().getType() != Material.AIR) {
                continue;
            }

            player.sendBlockChange(loc, GLASS_DATA);
        }

        for (Location loc : blocksNotLongerPart) {
            if (loc.getBlock().getType() != Material.AIR) {
                continue;
            }

            player.sendBlockChange(loc, AIR_DATA);
        }
    }

    void removePlatform(Location location, Player player) {
        Platform newPlatform = generate(location, 1, 1);

        for (Location loc : newPlatform.platformLocations) {
            if (loc.getBlock().getType() != Material.AIR) {
                continue;
            }

            player.sendBlockChange(loc, AIR_DATA);
        }
    }

    Platform generate(Location center, int xr, int zr) {
        center.setYaw(0);
        center.setPitch(0);

        List<Location> locs = new ArrayList<>();
        for (int i = -xr; i <= xr; i++) {
            for (int k = -zr; k <= zr; k++) {
                locs.add(center.clone().add(i, 0, k));
            }
        }

        return new Platform(locs);
    }

    void setBlockWithoutUpdating(Block block, Material type) {
        CraftWorld world = (CraftWorld) block.getWorld();
        Level nmsWorld = world.getHandle();

        BlockPos position = new BlockPos(block.getX(), block.getY(), block.getZ());
        LevelChunk chunk = nmsWorld.getChunkAt(position);

        CraftBlockData data = (CraftBlockData) type.createBlockData();

        chunk.setBlockState(position, data.getState(), false);
    }

    record Platform(List<Location> platformLocations) {

        public List<Location> subtract(Platform other) {
            List<Location> newLocations = new ArrayList<>(platformLocations);
            newLocations.removeAll(other.platformLocations);

            return newLocations;
        }
    }
}
