package me.fixeddev.troll.commands;

import me.fixeddev.troll.menu.TrollUserMenu;
import me.fixeddev.troll.user.User;
import me.fixeddev.troll.user.UserRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TrollCommand implements CommandExecutor {

    private final TrollUserMenu trollUserMenu;
    private final UserRegistry userRegistry;

    public TrollCommand(TrollUserMenu trollUserMenu, UserRegistry userRegistry) {
        this.trollUserMenu = trollUserMenu;
        this.userRegistry = userRegistry;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            return false;
        }

        if (!(sender instanceof Player senderPlayer)) {
            sender.sendMessage(Component.translatable("troll.only-players"));

            return true;
        }

        String playerName = args[0];
        Player player = Bukkit.getPlayer(playerName);

        if (player == null) {
            sender.sendMessage(Component.translatable("troll.user-offline", Component.text(playerName)));

            return true;
        }

        Optional<User> optUser = userRegistry.getIfCached(player.getUniqueId());
        Optional<User> optSender = userRegistry.getIfCached(senderPlayer.getUniqueId());

        if (optSender.isEmpty() || optUser.isEmpty()) {
            sender.sendMessage(Component.translatable("troll.error"));

            return true;
        }

        User userSender = optSender.get();
        User userTarget = optUser.get();

        trollUserMenu.generateGui(userTarget, userSender).show(senderPlayer);
        senderPlayer.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);

        return true;
    }
}
