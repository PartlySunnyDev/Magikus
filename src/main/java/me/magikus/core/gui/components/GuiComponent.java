package me.magikus.core.gui.components;

import me.magikus.Magikus;
import me.magikus.core.gui.MagikusGui;
import me.magikus.core.tools.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class GuiComponent implements Listener {

    protected final MagikusGui parent;
    protected final UUID uniqueID = UUID.randomUUID();
    private final String id;
    @Nullable
    private ItemStack shownItem;

    public GuiComponent(String id, @Nullable ItemStack shownItem, MagikusGui parent) {
        this.id = id;
        this.shownItem = shownItem;
        this.parent = parent;
        Bukkit.getServer().getPluginManager().registerEvents(this, Magikus.getPlugin(Magikus.class));
    }

    public String id() {
        return id;
    }

    @Nullable
    public ItemStack shownItem() {
        return shownItem;
    }

    public void setShownItem(@Nullable ItemStack shownItem) {
        ItemUtils.setUniqueId(shownItem, uniqueID);
        this.shownItem = shownItem;
    }
}
