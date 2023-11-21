package me.fixeddev.troll.user;

import it.unimi.dsi.fastutil.Pair;
import me.fixeddev.troll.troll.TrollType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PojoUser implements User {

    private final UUID id;
    private final List<Map.Entry<UUID, TrollType>> trollHistory;

    public PojoUser(UUID id, List<Map.Entry<UUID, TrollType>> trollHistory) {
        this.id = id;
        this.trollHistory = trollHistory;
    }


    @Override
    public UUID id() {
        return id;
    }

    @Override
    public int trolledTimes() {
        return trollHistory.size();
    }

    @Override
    public Optional<UUID> lastWhoTrolled() {
        if (!trollHistory.isEmpty()) {
            Map.Entry<UUID, TrollType> pair = trollHistory.get(trollHistory.size() - 1);

            return Optional.ofNullable(pair != null ? pair.getKey(): null);
        }

        return Optional.empty();
    }

    @Override
    public List<Map.Entry<UUID, TrollType>> trollHistory() {
        return trollHistory;
    }
}
