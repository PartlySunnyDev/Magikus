package me.magikus.recipeManager;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RecipeListener implements Listener {

    private static final List<CustomRecipe> recipes = new ArrayList<>();

    public static void registerRecipe(CustomRecipe r) {
        recipes.add(r);
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent e) {
        CraftingInventory ci = e.getInventory();
        ItemStack result = ci.getResult();
        if (result == null) {
            return;
        }
        CustomRecipe recipeInsideCraftingTable = null;
        NBTItem nbtiCurrentResult = new NBTItem(result);
        for (CustomRecipe r : recipes) {
            ItemStack recipeResult = r.result();
            NBTItem nbtiResult = new NBTItem(recipeResult);
            if (nbtiResult.hasKey("custom_id") && nbtiCurrentResult.hasKey("custom_id")) {
                if (nbtiCurrentResult.getString("custom_id").equals(nbtiResult.getString("custom_id"))) {
                    recipeInsideCraftingTable = r;
                    break;
                }
            } else if (result.isSimilar(recipeResult)) {
                recipeInsideCraftingTable = r;
                break;
            }
        }
        if (recipeInsideCraftingTable == null) {
            return;
        }
        ItemStack[] recipe = recipeInsideCraftingTable.recipe;
        ItemStack[] craftingGrid = ci.getMatrix();
        int count = 0;
        for (ItemStack i : craftingGrid) {
            if (i.getAmount() < recipe[count].getAmount()) {
                ci.setResult(null);
                return;
            }
            NBTItem gridItemNbt = new NBTItem(i);
            NBTItem realItemNbt = new NBTItem(recipe[count]);
            if (gridItemNbt.hasKey("custom_id") && realItemNbt.hasKey("custom_id")) {
                if (!gridItemNbt.getString("custom_id").equals(realItemNbt.getString("custom_id"))) {
                    ci.setResult(null);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        CraftingInventory ci = e.getInventory();
        ItemStack result = ci.getResult();
        if (result == null) {
            return;
        }
        CustomRecipe recipeInsideCraftingTable = null;
        NBTItem nbtiCurrentResult = new NBTItem(result);
        for (CustomRecipe r : recipes) {
            ItemStack recipeResult = r.result();
            NBTItem nbtiResult = new NBTItem(recipeResult);
            if (nbtiResult.hasKey("custom_id") && nbtiCurrentResult.hasKey("custom_id")) {
                if (nbtiCurrentResult.getString("custom_id").equals(nbtiResult.getString("custom_id"))) {
                    recipeInsideCraftingTable = r;
                    break;
                }
            } else if (result.isSimilar(recipeResult)) {
                recipeInsideCraftingTable = r;
                break;
            }
        }
        if (recipeInsideCraftingTable == null) {
            return;
        }
        e.setCancelled(true);
        ItemStack[] recipe = recipeInsideCraftingTable.recipe;
        ItemStack[] craftingGrid = ci.getMatrix();
        int count = 0;
        for (ItemStack i : craftingGrid) {
            i.setAmount(i.getAmount() - recipe[count].getAmount());
            if (i.getAmount() < 1) {
                i.setType(Material.AIR);
            }
        }
    }

}
