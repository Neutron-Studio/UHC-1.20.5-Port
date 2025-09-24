package gg.uhc.uhc.inventory;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class IconInventory implements IconUpdateListener, Listener {

    protected final List<IconStack> icons = Lists.newArrayList();

    protected final String title;
    protected Inventory inventory;

    public IconInventory(String title) {
        this.title = title;
        inventory = Bukkit.createInventory(null, 9, title);
    }

    public void showTo(HumanEntity entity) {
        entity.openInventory(inventory);
    }

    public void registerNewIcon(IconStack icon) {
        icon.registerUpdateHandler(this);
        add(icon);
    }

    protected int add(IconStack icon) {
        int index = indexToInsert(icon);
        icons.add(index, icon);

        ensureInventorySize();

        for (int i = index; i < icons.size(); i++) {
            inventory.setItem(i, icons.get(i));
        }

        return index;
    }

    protected int indexToInsert(IconStack icon) {
        int insertionIndex = Collections.binarySearch(icons, icon);

        if (insertionIndex < 0) {
            insertionIndex = -(insertionIndex + 1);
        }

        return insertionIndex;
    }

    @Override
    public void onUpdate(IconStack icon) {
        int index = icons.indexOf(icon);

        if (index < 0) return;

        inventory.setItem(index, icon);
    }

    @Override
    public void onWeightUpdate(IconStack icon) {
        int index = icons.indexOf(icon);

        if (index < 0) return;

        icons.remove(index);

        int newIndex = add(icon);

        for (int i = newIndex; i < index && i < icons.size(); i++) {
            inventory.setItem(i, icons.get(i));
        }
    }

    protected void ensureInventorySize() {
        int iconCount = icons.size();

        int slotsRequired = Math.max(9, Math.min(54, 9 * ((iconCount + 8) / 9)));

        if (slotsRequired == inventory.getSize()) return;

        inventory.clear();

        Inventory newInventory = Bukkit.createInventory(null, slotsRequired, title);

        ItemStack[] contents = new ItemStack[slotsRequired];
        for (int i = 0; i < icons.size(); i++) {
            contents[i] = icons.get(i);
        }

        newInventory.setContents(contents);

        for (HumanEntity entity : inventory.getViewers()) {
            entity.closeInventory();
            entity.openInventory(newInventory);
        }

        inventory = newInventory;
    }

    @EventHandler(ignoreCancelled = true)
    public void on(InventoryClickEvent event) {
        if (!event.getView().getTopInventory().equals(inventory)) return;

        event.setCancelled(true);

        int slot = event.getRawSlot();

        if (slot < 0 || slot >= inventory.getSize()) return;
        if (slot >= icons.size()) return;

        IconStack icon = icons.get(slot);
        if (icon == null) return;

        if (event.getWhoClicked() instanceof Player player) {
            icon.onClick(player);
        }
    }
}
