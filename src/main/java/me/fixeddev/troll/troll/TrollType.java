package me.fixeddev.troll.troll;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface TrollType {
    String id();

    Component displayName();

    void executeTroll(Player troll, Player trolled);
}
