package me.magikus.recipeManager;

import me.magikus.Magikus;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class CustomRecipe {

    private final ItemStack result;
    private final Map<Character, ItemStack> ingredients = new HashMap<>();
    private final ShapedRecipe r;
    ItemStack[] recipe = new ItemStack[9];
    String shape = "         ";

    public CustomRecipe(ItemStack result, String id) {
        this.result = result;
        r = new ShapedRecipe(new NamespacedKey(JavaPlugin.getPlugin(Magikus.class), id), result);
    }

    public void shape(String shape) {
        if (shape.length() != 9) {
            throw new IllegalArgumentException("Shape must be 9 characters long");
        }
        this.shape = shape;
        reloadRecipe();
    }

    public void setIngredient(char i, ItemStack item) {
        ingredients.put(i, item);
        reloadRecipe();
    }

    public void reloadRecipe() {
        int count = 0;
        for (char c : shape.toCharArray()) {
            if (c == ' ') {
                recipe[count] = null;
            } else if (ingredients.containsKey(c)) {
                recipe[count] = ingredients.get(c);
            }
        }
        r.shape(shape.substring(0, 3), shape.substring(3, 6), shape.substring(6, 9));
        for (char c : ingredients.keySet()) {
            ItemStack item = ingredients.get(c);
            if (item != null) {
                r.setIngredient(c, item);
            }
        }
    }

    public ItemStack result() {
        return result;
    }

    public void register() {
        RecipeListener.registerRecipe(this);
    }

    public ShapedRecipe r() {
        return r;
    }
}
