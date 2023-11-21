package me.fixeddev.troll.user;

import it.unimi.dsi.fastutil.Pair;
import me.fixeddev.troll.troll.TrollType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface User {
    /**
     * The id of the user.
     *
     * @return A {@linkplain UUID}
     */
    UUID id();

    /**
     * Calculates the number of times that this user has been trolled.
     *
     * @return An integer.
     */
    int trolledTimes();

    /**
     * Gets the last {@linkplain User}'s id that trolled this User.
     *
     * @return An {@linkplain Optional} of {@linkplain UUID}.
     */
    Optional<UUID> lastWhoTrolled();

    /**
     * All the history of trolls that happened to this user.
     *
     * @return A {@linkplain List} of pairs<Troll's id, {@linkplain TrollType}>
     */
    List<Map.Entry<UUID, TrollType>> trollHistory();
}
