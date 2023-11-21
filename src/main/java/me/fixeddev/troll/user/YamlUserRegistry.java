package me.fixeddev.troll.user;

import me.fixeddev.troll.troll.TrollType;
import me.fixeddev.troll.troll.TrollTypesRegistry;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public final class YamlUserRegistry extends AbstractUserRegistry {

    private final File dataFolder;
    private final TrollTypesRegistry trollTypesRegistry;

    public YamlUserRegistry(File dataFolder, TrollTypesRegistry trollTypesRegistry) {
        this.dataFolder = dataFolder;
        this.trollTypesRegistry = trollTypesRegistry;
    }

    @Override
    public CompletableFuture<Void> save(User user) {
        File file;
        YamlConfiguration userFile;

        try {
            file = getFileOf(user.id());
            userFile = getYamlFile(file);
        } catch (IOException | InvalidConfigurationException e) {
            return CompletableFuture.failedFuture(e);
        }

        saveToFile(userFile, user);
        try {
            userFile.save(file);
        } catch (IOException e) {
            return CompletableFuture.failedFuture(e);
        }

        return CompletableFuture.completedFuture(null);
    }

    @Override
    protected CompletableFuture<User> _find(UUID uuid) {
        File file;
        YamlConfiguration userFile;

        try {
            file = getFileOf(uuid);
            userFile = getYamlFile(file);
        } catch (IOException | InvalidConfigurationException e) {
            return CompletableFuture.failedFuture(e);
        }

        User user = loadFromFile(userFile);

        return CompletableFuture.completedFuture(user);
    }

    private User loadFromFile(YamlConfiguration userFile) {
        String userIdStr = Objects.requireNonNull(userFile.getString("user.id")); // we know that it isn't null
        UUID userId = UUID.fromString(userIdStr);

        List<Map.Entry<UUID, TrollType>> trollsList = new ArrayList<>();
        for (String troll : userFile.getStringList("user.trolls")) {
            String[] split = troll.split(":");

            UUID trollId = UUID.fromString(split[0]);
            String trollTypeId = split[1];
            trollTypesRegistry.byId(trollTypeId).ifPresent(trollType -> trollsList.add(Map.entry(trollId, trollType)));

        }

        return new PojoUser(userId, trollsList);
    }

    private void saveToFile(YamlConfiguration userFile, User user) {
        userFile.set("user.id", user.id().toString());
        List<String> trollsList = new ArrayList<>();

        for (Map.Entry<UUID, TrollType> pair : user.trollHistory()) {
            trollsList.add(pair.getKey() + ":" + pair.getValue().id());
        }

        userFile.set("user.trolls", trollsList);
    }

    private File getFileOf(UUID userId) throws IOException {
        File file = new File(dataFolder, userId + ".yml");
        if (!file.exists()) {
            file.createNewFile();
        }

        return file;
    }

    private YamlConfiguration getYamlFile(File file) throws IOException, InvalidConfigurationException {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.load(file);

        return configuration;
    }
}
