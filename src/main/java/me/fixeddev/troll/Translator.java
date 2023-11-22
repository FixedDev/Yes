package me.fixeddev.troll;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.plugin.Plugin;

public class Translator {


    private Plugin plugin;

    public Translator(Plugin plugin) {
        this.plugin = plugin;
    }

    public void send(Audience audience, Component component, TagResolver... resolvers) {
        audience.sendMessage(translate(component, resolvers));
    }

    public void send(Audience audience, TranslatableComponent component, TagResolver... resolvers) {
        audience.sendMessage(translate(component, resolvers));
    }

    public void send(Audience audience, String path, TagResolver... resolvers) {
        audience.sendMessage(translate(path, resolvers));
    }

    public Component translate(Component component, TagResolver... resolvers) {
        if (!(component instanceof TranslatableComponent translatableComponent)) {
            return component;
        }

        return translate(translatableComponent, resolvers);
    }

    public Component translate(TranslatableComponent component, TagResolver... resolvers) {
        String translation = plugin.getConfig().getString("messages." + component.key(), component.fallback());

        if (translation == null) {
            translation = "messages." + component.key();
        }

        translation = translation.formatted(component.args().stream()
                .map(arg -> MiniMessage.miniMessage().serialize(arg)).toArray());

        return parse(translation, resolvers);
    }

    public Component translate(String path, TagResolver... resolvers) {
        String translation = plugin.getConfig().getString("messages." + path, "messages." + path);

        return parse(translation, resolvers);
    }

    private Component parse(String text, TagResolver... resolvers) {
        return MiniMessage.miniMessage().deserialize(text, TagResolver.resolver(resolvers));
    }
}
