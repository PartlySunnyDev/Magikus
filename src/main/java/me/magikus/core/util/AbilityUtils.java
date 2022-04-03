package me.magikus.core.util;

import de.tr7zw.nbtapi.NBTItem;
import me.magikus.core.items.ItemUpdater;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.items.abilities.Ability;
import me.magikus.core.items.additions.IAbilityAddition;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

public class AbilityUtils {

    public static boolean hasAbility(ItemStack i, String type) {
        if (i == null || i.getType() == Material.AIR) {
            return false;
        }
        NBTItem nbti = new NBTItem(i);
        if (!nbti.getBoolean("mg_unique")) {
            return false;
        }
        UUID mg_unique_id = nbti.getUUID("mg_unique_id");
        if (!ItemUpdater.items.containsKey(mg_unique_id)) {
            ItemUpdater.registerItem(i);
        }
        MagikusItem mgi = ItemUpdater.items.get(mg_unique_id);
        if (mgi.getAbilities() != null) {
            for (Ability a : mgi.getAbilities().asList()) {
                if (Objects.equals(a.id(), type)) {
                    return true;
                }
            }
        }
        for (IAbilityAddition a : mgi.abilityAdditions().asAbilityList()) {
            if (Objects.equals(a.getAbilities().id(), type)) {
                return true;
            }
        }
        return false;
    }

}
