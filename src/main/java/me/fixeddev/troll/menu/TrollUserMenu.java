package me.fixeddev.troll.menu;

import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.MasonryPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.fixeddev.troll.Translator;
import me.fixeddev.troll.troll.TrollType;
import me.fixeddev.troll.troll.TrollTypesRegistry;
import me.fixeddev.troll.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class TrollUserMenu {

    private final TrollTypesRegistry trollTypesRegistry;
    private final Translator translator;

    public TrollUserMenu(TrollTypesRegistry trollTypesRegistry, Translator translator) {
        this.trollTypesRegistry = trollTypesRegistry;
        this.translator = translator;
    }

    public Gui generateGui(User target, User troll) {
        ChestGui chestGui = new ChestGui(3, ComponentHolder.of(translator.translate("troll.menu.title", Placeholder.unparsed("target", Bukkit.getPlayer(target.id()).getName()))));

        chestGui.setOnGlobalClick(clickEvent -> {
            clickEvent.setCancelled(true);
        });

        MasonryPane masonryPane = new MasonryPane(9, 3);
        masonryPane.addPane(generateDecorativePane(9));
        masonryPane.addPane(addTrollItems(target, troll));
        masonryPane.addPane(generateDecorativePane(9));

        chestGui.addPane(masonryPane);

        return chestGui;
    }

    private Pane addTrollItems(User target, User troll) {
        StaticPane staticPane = new StaticPane(9, 1);
        Collection<TrollType> trollTypes = trollTypesRegistry.allTypes();

        ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        blackPane.editMeta(itemMeta -> itemMeta.displayName(Component.text(" ", NamedTextColor.BLACK)));

        int x = 0;
        for (TrollType trollType : trollTypes) {
            staticPane.addItem(generateItem(trollType, target, troll), x++, 0);
            staticPane.addItem(new GuiItem(blackPane), x++, 0);
        }

        staticPane.fillWith(blackPane);

        return staticPane;
    }

    private GuiItem generateItem(TrollType trollType, User target, User troll) {
        ItemStack itemStack = new ItemStack(Material.PAPER);

        itemStack.editMeta(itemMeta -> itemMeta.displayName(translator.translate(trollType.displayName())));

        return new GuiItem(itemStack, inventoryClickEvent -> {
            trollType.executeTroll(troll, target);
        });
    }

    private Pane generateDecorativePane(int length) {
        StaticPane staticPane = new StaticPane(length, 1);
        ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        blackPane.editMeta(itemMeta -> itemMeta.displayName(Component.text(" ", NamedTextColor.BLACK)));

        staticPane.fillWith(blackPane);

        return staticPane;
    }
}
