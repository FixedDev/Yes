package me.fixeddev.troll.troll;

import java.util.Optional;

public interface TrollTypesRegistry {
    /**
     * Tries to obtain a {@linkplain TrollType} if registered.
     *
     * @param id The id of the {@linkplain TrollType} to search for.
     * @return A {@linkplain Optional} that might contain a {@linkplain TrollType}
     */
    Optional<TrollType> byId(String id);

    /**
     * Registers the specified {@linkplain TrollType} into this registry.
     *
     * @param trollType The {@linkplain TrollType} to register.
     * @throws IllegalArgumentException If a TrollType with the same id is already registered.
     */
    void register(TrollType trollType);

    /**
     * Removes the specified {@linkplain TrollType} from this registry if found.
     *
     * @param trollType The {@linkplain TrollType} to unregister.
     */
    void unregister(TrollType trollType);
}
