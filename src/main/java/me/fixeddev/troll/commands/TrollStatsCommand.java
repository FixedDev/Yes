package me.fixeddev.troll.commands;

import me.fixeddev.troll.Translator;
import me.fixeddev.troll.menu.TrollStatsMenu;
import me.fixeddev.troll.menu.TrollUserMenu;
import me.fixeddev.troll.user.User;
import me.fixeddev.troll.user.UserRegistry;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TrollStatsCommand implements CommandExecutor {

    private final TrollStatsMenu trollUserMenu;
    private final UserRegistry userRegistry;
    private final Translator translator;

    public TrollStatsCommand(TrollStatsMenu trollUserMenu, UserRegistry userRegistry, Translator translator) {
        this.trollUserMenu = trollUserMenu;
        this.userRegistry = userRegistry;
        this.translator = translator;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            return false;
        }

        if (!(sender instanceof Player senderPlayer)) {
            translator.send(sender, "troll.only-players");

            return true;
        }

        String playerName = args[0];
        Player player = Bukkit.getPlayer(playerName);

        if (player == null) {
            translator.send(sender, "troll.user-offline", Placeholder.unparsed("target", playerName));

            return true;
        }

        Optional<User> optUser = userRegistry.getIfCached(player.getUniqueId());

        if (optUser.isEmpty()) {
            translator.send(sender, "troll.error");

            return true;
        }

        User userTarget = optUser.get();

        trollUserMenu.generateGui(userTarget).show(senderPlayer);
        senderPlayer.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0);

        return true;
    }
}
