package me.fixeddev.troll.listeners;

import me.fixeddev.troll.user.PojoUser;
import me.fixeddev.troll.user.User;
import me.fixeddev.troll.user.UserRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListeners implements Listener {

    private final UserRegistry userRegistry;

    public UserListeners(UserRegistry userRegistry) {
        this.userRegistry = userRegistry;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        userRegistry.getOrFind(event.getPlayer().getUniqueId()).thenAccept(user -> {
            if (user == null) {
                user = new PojoUser(event.getPlayer().getUniqueId());

                userRegistry.cache(user);
                userRegistry.save(user);
            }
        }); // first load the user.
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        userRegistry.getIfCached(event.getPlayer().getUniqueId())
                .ifPresent(user -> {
                    userRegistry.save(user); // save if not saved.
                    userRegistry.uncache(user.id());
                });
    }
}
