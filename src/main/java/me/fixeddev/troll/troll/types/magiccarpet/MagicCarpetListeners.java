package me.fixeddev.troll.troll.types.magiccarpet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MagicCarpetListeners implements Listener {

    private final MagicCarpetHandler magicCarpetHandler;

    public MagicCarpetListeners(MagicCarpetHandler magicCarpetHandler) {
        this.magicCarpetHandler = magicCarpetHandler;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (event.hasChangedBlock() && magicCarpetHandler.hasMagicCarpet(player)) {
            Location beforeLocation = event.getFrom().toCenterLocation().subtract(0, 1, 0);
            Location afterLocation = event.getTo().toCenterLocation().subtract(0, 1, 0);

            Block belowBlockBefore = beforeLocation.getBlock();

            Material oldMaterial = magicCarpetHandler.popLast(player);

            if (oldMaterial != null) {
                magicCarpetHandler.setBlockWithoutUpdating(belowBlockBefore, oldMaterial);
            }

            Block belowBlockNow = afterLocation.getBlock();
            if (!belowBlockNow.isBuildable()) {
                magicCarpetHandler.setBlock(player, belowBlockNow, Material.GLASS, true);
                belowBlockNow.setType(Material.GLASS);
            }

            magicCarpetHandler.updatePlatform(beforeLocation, afterLocation, player);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        magicCarpetHandler.stopMagicCarpet(event.getPlayer());
    }
}
