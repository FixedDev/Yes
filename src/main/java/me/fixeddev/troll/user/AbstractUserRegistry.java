package me.fixeddev.troll.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractUserRegistry implements UserRegistry {
    private final Map<UUID, User> userCache;

    public AbstractUserRegistry() {
        userCache = new HashMap<>();
    }

    @Override
    public final CompletableFuture<User> find(UUID userId) {
        return _find(userId).thenApply(user -> {
            if (!userCache.containsKey(userId)) {
                cache(user);
            }
            return user;
        });
    }

    @Override
    public final CompletableFuture<User> getOrFind(UUID userId) {
        return getIfCached(userId)
                .map(CompletableFuture::completedFuture)
                .orElseGet(() -> find(userId));
    }

    @Override
    public final Optional<User> getIfCached(UUID userId) {
        return Optional.ofNullable(userCache.get(userId));
    }

    @Override
    public final void uncache(UUID userId) {
        userCache.remove(userId);
    }

    @Override
    public final void cache(User user) {
        if (user == null) {
            return;
        }

        userCache.put(user.id(), user);
    }

    @Override
    public void load() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            getOrFind(onlinePlayer.getUniqueId());
        }
    }

    @Override
    public void save() {
        userCache.forEach((uuid, user) -> {
            save(user);
            uncache(uuid);
        });
    }

    @Override
    public abstract CompletableFuture<Void> save(User user);


    protected abstract CompletableFuture<User> _find(UUID uuid);

}
