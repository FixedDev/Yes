package me.fixeddev.troll.user;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserRegistry {
    /**
     * Finds a {@linkplain User} in the datastore, adds it to the cache and returns it.
     *
     * @param userId The {@linkplain UUID} of the user to search
     * @return A {@linkplain CompletableFuture} of {@linkplain User}, User value may be null if not found.
     */
    CompletableFuture<User> find(UUID userId);

    /**
     * Tries searching a {@linkplain User} in the cache, if found, returns it, otherwise searches it on the datastore
     * and adds it if found.
     *
     * @param userId The {@linkplain UUID} of the user to search
     * @return A {@linkplain CompletableFuture} of {@linkplain User}, User value may be null if not found.
     */
    CompletableFuture<User> getOrFind(UUID userId);

    /**
     * Searches a {@linkplain User} in the cache, if found returns it.
     *
     * @param userId The {@linkplain UUID} of the user to search
     * @return A {@linkplain Optional} of {@linkplain User}.
     */
    Optional<User> getIfCached(UUID userId);

    /**
     * Saves the {@linkplain User} into the backing datastore.
     *
     * @param user The User to save.
     * @return A {@linkplain CompletableFuture} that signals when the saving is complete.
     */
    CompletableFuture<Void> save(User user);

    /**
     * Explicitly removes a {@linkplain User} from the cache.
     *
     * @param userId The id of the User to remove from the cache.
     */
    void uncache(UUID userId);

    /**
     * Explicitly adds a {@linkplain User} to the cache.
     *
     * @param user The User to add to the cache.
     */
    void cache(User user);

    void load();

    void save();
}
