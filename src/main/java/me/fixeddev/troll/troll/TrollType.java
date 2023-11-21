package me.fixeddev.troll.troll;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

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

    void executeTroll(Player troll, Player trolled);
}
