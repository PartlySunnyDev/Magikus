package me.magikus.core.util;

import de.tr7zw.nbtapi.NBTItem;
import me.magikus.Magikus;
import me.magikus.core.ConsoleLogger;
import me.magikus.core.enums.Rarity;
import me.magikus.core.enums.VanillaArmorAttributes;
import me.magikus.core.enums.VanillaDamageAttributes;
import me.magikus.core.enums.VanillaSpellCastables;
import me.magikus.core.items.ItemType;
import me.magikus.core.items.ItemUpdater;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.items.abilities.AbilityList;
import me.magikus.core.items.additions.ascensions.Ascension;
import me.magikus.core.items.additions.ascensions.AscensionManager;
import me.magikus.core.stats.Stat;
import me.magikus.core.stats.StatList;
import me.magikus.core.stats.StatType;
import me.magikus.core.util.classes.Pair;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.HashMap;

public class DataUtils {

    public static boolean isSame(ItemStack a, ItemStack b) {
        NBTItem an = new NBTItem(a);
        NBTItem bn = new NBTItem(b);
        return an.getUUID("mg_unique_id") == bn.getUUID("mg_unique_id");
    }

    public static ItemType getTypeOfItem(Material s) {
        final String typeNameString = s.name();
        ItemType type = ItemType.ITEM;
        if (typeNameString.endsWith("_HELMET")) {
            type = ItemType.HELMET;
        } else if (typeNameString.endsWith("_CHESTPLATE")) {
            type = ItemType.CHESTPLATE;
        } else if (typeNameString.endsWith("_LEGGINGS")) {
            type = ItemType.LEGGINGS;
        } else if (typeNameString.endsWith("_BOOTS")) {
            type = ItemType.BOOTS;
        } else if (typeNameString.endsWith("_SWORD")) {
            type = ItemType.SWORD;
        } else if (typeNameString.endsWith("_PICKAXE")) {
            type = ItemType.PICKAXE;
        } else if (typeNameString.endsWith("_SHOVEL")) {
            type = ItemType.SHOVEL;
        } else if (typeNameString.endsWith("_HOE")) {
            type = ItemType.HOE;
        } else if (typeNameString.endsWith("_AXE")) {
            type = ItemType.AXE;
        } else if (typeNameString.endsWith("BOW")) {
            type = ItemType.BOW;
        }
        return type;
    }

    public static MagikusItem getMagikusItem(ItemStack stack, Player p) {
        if (stack == null || stack.getType() == Material.AIR) {
            return null;
        }
        NBTItem i = new NBTItem(stack);
        if (i.getBoolean("mg_unique")) {
            if (!ItemUpdater.items.containsKey(i.getUUID("mg_unique_id"))) {
                ItemUpdater.registerItem(stack, p);
            }
            return ItemUpdater.items.get(i.getUUID("mg_unique_id"));
        } else {
            if (i.getBoolean("vanilla")) {
                return createMagikusItemFromVanilla(stack, p);
            }
        }
        return null;
    }

    public static MagikusItem getMagikusItem(ItemStack stack) {
        return getMagikusItem(stack, null);
    }

    public static MagikusItem createMagikusItemFromVanilla(ItemStack s, @Nullable Player player) {
        NBTItem nbti = new NBTItem(s);
        if (nbti.getBoolean("vanilla")) {
            MagikusItem magikusItem = new MagikusItem(s.getType().toString().toLowerCase(), false, getTypeOfItem(s.getType()), player, true) {
                @Override
                public Material getDefaultItem() {
                    return s.getType();
                }

                @Override
                public String getDisplayName() {
                    return TextUtils.capitalizeWord(s.getType().toString().toLowerCase().replace("_", " "));
                }

                @Override
                public AbilityList getAbilities() {
                    return null;
                }

                @Override
                public boolean isEnchanted() {
                    return false;
                }

                @Override
                public StatList getStats() {
                    return getVanillaStats(s.getType());
                }

                @Override
                public String getDescription() {
                    return "";
                }

                @Override
                public Rarity getRarity() {
                    return Rarity.NORMAL;
                }

                @Override
                public boolean canCastSpells() {
                    try {
                        VanillaSpellCastables a = VanillaSpellCastables.valueOf(s.getType().toString().toUpperCase());
                        return true;
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                }
            };
            magikusItem.setStackCount(s.getAmount());
            magikusItem.statAdditions().addAdditions(nbti.getCompound("statAdditions"));
            magikusItem.abilityAdditions().addAdditions(nbti.getCompound("abilityAdditions"));
            magikusItem.rarityAdditions().addAdditions(nbti.getCompound("rarityAdditions"));
            return magikusItem;
        }
        return null;
    }

    public static ItemStack setVanillaStats(ItemStack i) {
        NBTItem nbti = new NBTItem(i);
        try {
            ItemUtils.setItemStats(nbti, new Stat(StatType.DAMAGE, VanillaDamageAttributes.valueOf(i.getType().toString()).getDamage() * 5));
        } catch (Exception ignored) {
        }
        try {
            ItemUtils.setItemStats(nbti, new Stat(StatType.DEFENSE, VanillaArmorAttributes.valueOf(i.getType().toString()).getDefense()));
        } catch (Exception ignored) {
        }
        return nbti.getItem();
    }

    public static StatList getVanillaStats(Material i) {
        StatList stl = new StatList();
        try {
            stl.addStat(new Stat(StatType.DAMAGE, VanillaDamageAttributes.valueOf(i.toString()).getDamage() * 5));
        } catch (Exception ignored) {
        }
        try {
            stl.addStat(new Stat(StatType.DEFENSE, VanillaArmorAttributes.valueOf(i.toString()).getDefense()));
        } catch (Exception ignored) {
        }
        return stl;
    }

    public static MagikusItem createMagikusItemFromVanilla(Material s, @Nullable Player player) {
        return new MagikusItem(s.toString().toLowerCase(), false, getTypeOfItem(s), player, true) {
            @Override
            public Material getDefaultItem() {
                return s;
            }

            @Override
            public String getDisplayName() {
                return TextUtils.capitalizeWord(s.toString().toLowerCase().replace("_", " "));
            }

            @Override
            public AbilityList getAbilities() {
                return null;
            }

            @Override
            public boolean isEnchanted() {
                return false;
            }

            @Override
            public StatList getStats() {
                return getVanillaStats(s);
            }

            @Override
            public String getDescription() {
                return "";
            }

            @Override
            public Rarity getRarity() {
                return Rarity.NORMAL;
            }
        };
    }

    public static StatList getStatsOfBest(String ascension, Rarity ra) {
        StatList addition;
        Ascension r = AscensionManager.getAscension(ascension);
        if (r == null) {
            ConsoleLogger.console("Illegal ascension " + ascension + " found on item");
            return null;
        }
        if (r.statBonuses().containsKey(ra)) {
            addition = r.statBonuses().get(ra);
        } else {
            Rarity bestRarity = Rarity.NORMAL;
            for (Rarity rarity : r.statBonuses().keySet()) {
                if (rarity.level() > bestRarity.level() && rarity.level() <= ra.level()) {
                    bestRarity = rarity;
                }
            }
            addition = r.statBonuses().get(bestRarity);
        }
        return addition;
    }

    public static StatList readStats(ItemStack stack) {
        NBTItem i = new NBTItem(stack);
        StatList r = new StatList();
        for (StatType s : StatType.values()) {
            if (i.hasKey(s.id())) {
                r.addStat(new Stat(s, i.getInteger(s.id())));
            }
        }
        return r;
    }

    @SafeVarargs
    public static HashMap<Rarity, StatList> getStatWithBonusModifiersFrom(Pair<Rarity, StatList>... stats) {
        HashMap<Rarity, StatList> r = new HashMap<>();
        for (Pair<Rarity, StatList> s : stats) {
            r.put(s.a(), s.b());
        }
        return r;
    }

    @SuppressWarnings("all")
    public static Object getData(String key, PersistentDataType type, Entity e) {
        return e.getPersistentDataContainer().get(new NamespacedKey(JavaPlugin.getPlugin(Magikus.class), key), type);
    }

    @SuppressWarnings("all")
    public static void setData(String key, Object value, PersistentDataType type, Entity e) {
        e.getPersistentDataContainer().set(new NamespacedKey(JavaPlugin.getPlugin(Magikus.class), key), type, value);
    }

}
