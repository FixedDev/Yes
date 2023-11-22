package me.fixeddev.troll.menu;

import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.fixeddev.troll.troll.TrollType;
import me.fixeddev.troll.troll.TrollTypesRegistry;
import me.fixeddev.troll.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class TrollUserMenu {

    private final TrollTypesRegistry trollTypesRegistry;

    public TrollUserMenu(TrollTypesRegistry trollTypesRegistry) {
        this.trollTypesRegistry = trollTypesRegistry;
    }

    public Gui generateGui(User target, User troll) {
        ChestGui chestGui = new ChestGui(3, ComponentHolder.of(Component.translatable("troll.menu.title")));

        chestGui.setOnGlobalClick(clickEvent -> {
            clickEvent.setCancelled(true);
        });

        Pane nineLengthDecorativePane = generateDecorativePane(9);
        chestGui.addPane(nineLengthDecorativePane);
        chestGui.addPane(addTrollItems(target, troll));
        chestGui.addPane(nineLengthDecorativePane);

        return chestGui;
    }

    private Pane addTrollItems(User target, User troll) {
        StaticPane staticPane = new StaticPane(9, 1);
        Collection<TrollType> trollTypes = trollTypesRegistry.allTypes();

        int x = 0;
        for (TrollType trollType : trollTypes) {
            staticPane.addItem(generateItem(trollType, target, troll), x++, 0);
        }

        ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS);
        blackPane.editMeta(itemMeta -> itemMeta.displayName(Component.text(" ", NamedTextColor.BLACK)));

        staticPane.fillWith(blackPane);

        return staticPane;
    }

    private GuiItem generateItem(TrollType trollType, User target, User troll) {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        itemStack.editMeta(itemMeta -> itemMeta.displayName(trollType.displayName()));

        return new GuiItem(itemStack, inventoryClickEvent -> {
            trollType.executeTroll(troll, target);
        });
    }

    private Pane generateDecorativePane(int length) {
        StaticPane staticPane = new StaticPane(length, 1);
        ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS);
        blackPane.editMeta(itemMeta -> itemMeta.displayName(Component.text(" ", NamedTextColor.BLACK)));

        staticPane.fillWith(blackPane);

        return staticPane;
    }
}
