package me.fixeddev.troll.troll.types;

import me.fixeddev.troll.troll.TrollType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class KnockbackZombieTroll implements TrollType {

    private final Plugin plugin;

    public KnockbackZombieTroll(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String id() {
        return "kb-zombie";
    }

    @Override
    public Component displayName() {
        return Component.translatable("kb-zombie");
    }

    @Override
    public void executeTroll(Player troll, Player trolled) {
        Location location = trolled.getLocation();

        Zombie zombie = generateZombie(location, trolled);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            zombie.attack(trolled);
            zombie.swingMainHand();
        }, 10);

        Bukkit.getScheduler().runTaskLater(plugin, zombie::remove, 50);
    }

    @NotNull
    private Zombie generateZombie(Location location, Player target) {
        Zombie zombie = location.getWorld().spawn(location, Zombie.class);
        zombie.setShouldBurnInDay(false);
        zombie.setCanPickupItems(false);
        zombie.setInvulnerable(true);
        zombie.setTarget(target);

        zombie.addPotionEffect(PotionEffectType.SPEED.createEffect(70, 4)); // we want a very fast zombie

        EntityEquipment equipment = zombie.getEquipment();

        ItemStack itemStack = generateKBStick();
        equipment.setItemInMainHand(itemStack);

        return zombie;
    }

    @NotNull
    private ItemStack generateKBStick() {
        ItemStack itemStack = new ItemStack(Material.STICK);
        itemStack.addUnsafeEnchantment(Enchantment.KNOCKBACK, 50);
        return itemStack;
    }
}
