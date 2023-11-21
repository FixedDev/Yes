package me.fixeddev.troll.troll;

import me.fixeddev.troll.user.User;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public interface TrollType {
    /**
     * The id of this TrollType.
     *
     * @return The id of this TrollType.
     */
    String id();

    /**
     * The name to display in all messages of this TrollType.
     *
     * @return A {@linkplain Component} representing the display name of this TrollType.
     */
    Component displayName();

    default void executeTroll(User troll, User trolled) {
        executeTroll(Bukkit.getPlayer(troll.id()), Bukkit.getPlayer(trolled.id()));
        trolled.trollHistory().add(Map.entry(troll.id(), this));
    }

    void executeTroll(Player troll, Player trolled);
}
