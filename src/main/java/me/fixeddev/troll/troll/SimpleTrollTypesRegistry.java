package me.fixeddev.troll.troll;

import java.util.*;

public final class SimpleTrollTypesRegistry implements TrollTypesRegistry {

    private final Map<String, TrollType> trollTypeById;

    public SimpleTrollTypesRegistry() {
        trollTypeById = new LinkedHashMap<>();
    }

    @Override
    public Optional<TrollType> byId(String id) {
        return Optional.ofNullable(trollTypeById.get(id));
    }

    @Override
    public Collection<TrollType> allTypes() {
        return trollTypeById.values();
    }

    @Override
    public void register(TrollType trollType) {
        if (trollTypeById.containsKey(trollType.id())) {
            throw new IllegalArgumentException("A trolltype with id " + trollType.id() + " already exists!");
        }

        trollTypeById.put(trollType.id(), trollType);
    }

    @Override
    public void unregister(TrollType trollType) {
        trollTypeById.remove(trollType.id(), trollType);
    }
}
