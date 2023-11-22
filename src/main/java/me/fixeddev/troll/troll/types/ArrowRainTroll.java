package me.fixeddev.troll.troll.types;

import me.fixeddev.troll.Translator;
import me.fixeddev.troll.troll.TrollType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.*;

public class ArrowRainTroll implements TrollType {

    private final Plugin plugin;
    private final Translator translator;

    private final Random random = new Random();
    private final Set<UUID> playersOnRain;

    public ArrowRainTroll(Plugin plugin, Translator translator) {
        this.plugin = plugin;
        this.translator = translator;

        playersOnRain = new HashSet<>();
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            Iterator<UUID> playersOnRain = this.playersOnRain.iterator();

            while (playersOnRain.hasNext()) {
                Player player = Bukkit.getPlayer(playersOnRain.next());

                if (player == null) {
                    playersOnRain.remove();
                    continue;
                }

                spawnArrows(player);
            }
        }, 5, 2);
    }

    @Override
    public String id() {
        return "arrow-rain";
    }

    @Override
    public Component displayName() {
        return Component.translatable("troll.type.arrow-rain");
    }

    @Override
    public void executeTroll(Player troll, Player trolled) {
        playersOnRain.add(trolled.getUniqueId());
        translator.send(trolled, "troll.arrow-rain.good-luck");

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            playersOnRain.remove(trolled.getUniqueId());

        }, 120);
    }

    private void spawnArrows(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation().add(0, 10, 0);

        for (int i = 0; i < 12; i++) {
            Arrow arrow = world.spawn(location.clone().add(random.nextDouble(-2, 2), 0, random.nextDouble(-2, 2)), Arrow.class);
            arrow.setKnockbackStrength(20);

            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
            arrow.setGlowing(true);
        }
    }
}
