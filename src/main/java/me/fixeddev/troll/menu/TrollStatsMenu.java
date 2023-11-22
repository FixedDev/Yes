package me.fixeddev.troll.menu;

import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.fixeddev.troll.Translator;
import me.fixeddev.troll.troll.TrollType;
import me.fixeddev.troll.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TrollStatsMenu {
    private final Translator translator;

    public TrollStatsMenu(Translator translator) {
        this.translator = translator;
    }

    public Gui generateGui(User target) {
        ChestGui chestGui = new ChestGui(1, ComponentHolder.of(translator.translate("troll.stat-menu.title", Placeholder.unparsed("target", Bukkit.getPlayer(target.id()).getName()))));

        chestGui.setOnGlobalClick(clickEvent -> {
            clickEvent.setCancelled(true);
        });

        chestGui.addPane(addTrollItems(target));

        return chestGui;
    }

    private Pane addTrollItems(User target) {
        StaticPane staticPane = new StaticPane(9, 1);

        ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        blackPane.editMeta(itemMeta -> itemMeta.displayName(Component.text(" ", NamedTextColor.BLACK)));

        GuiItem lastTrollItem = target.lastWhoTrolled()
                .map(uuid -> new GuiItem(generateLastTrollItem(Bukkit.getOfflinePlayer(uuid).getName())))
                .orElseGet(() -> new GuiItem(generateLastTrollItem("Unknown")));

        staticPane.addItem(lastTrollItem, 3, 0);
        staticPane.addItem(new GuiItem(generateTrolledTimesItem(target.trolledTimes())), 4, 0);

        GuiItem lastTypeOfTrollItem = target.getLastTrollType()
                .map(trollType -> new GuiItem(generateLastTypeOfTroll(trollType)))
                .orElseGet(() -> new GuiItem(generateUnknownLastTypeOfTroll()));

        staticPane.addItem(lastTypeOfTrollItem, 5, 0);

        staticPane.fillWith(blackPane);

        return staticPane;
    }

    private ItemStack generateLastTrollItem(String name) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        itemStack.editMeta(itemMeta -> itemMeta.displayName(translator.translate("troll.stat-menu.last-troll", Placeholder.unparsed("player", name)).decoration(TextDecoration.ITALIC,false)));

        return itemStack;
    }

    private ItemStack generateTrolledTimesItem(int times) {
        ItemStack itemStack = new ItemStack(Material.REDSTONE);
        itemStack.editMeta(itemMeta -> itemMeta.displayName(translator.translate("troll.stat-menu.trolled-times", Placeholder.unparsed("times", String.valueOf(times))).decoration(TextDecoration.ITALIC,false)));

        return itemStack;
    }

    private ItemStack generateUnknownLastTypeOfTroll() {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        itemStack.editMeta(itemMeta -> itemMeta.displayName(translator.translate("troll.stat-menu.last-troll-type", Placeholder.unparsed("type", "Unknown")).decoration(TextDecoration.ITALIC,false)));

        return itemStack;
    }

    private ItemStack generateLastTypeOfTroll(TrollType trollType) {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        itemStack.editMeta(itemMeta -> itemMeta.displayName(translator.translate("troll.stat-menu.last-troll-type", Placeholder.component("type", translator.translate(trollType.displayName()))).decoration(TextDecoration.ITALIC,false)));

        return itemStack;
    }


}
