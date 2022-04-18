package me.magikus.core.items.lore;

import me.magikus.core.enums.Rarity;
import me.magikus.core.items.ItemType;
import me.magikus.core.items.ModifierType;
import me.magikus.core.items.abilities.Ability;
import me.magikus.core.items.abilities.AbilityList;
import me.magikus.core.items.abilities.AbilityType;
import me.magikus.core.items.additions.Addition;
import me.magikus.core.items.additions.AdditionInfo;
import me.magikus.core.items.additions.AdditionList;
import me.magikus.core.items.additions.IStatAddition;
import me.magikus.core.items.additions.ascensions.Ascension;
import me.magikus.core.items.additions.ascensions.AscensionManager;
import me.magikus.core.items.additions.enchants.Enchant;
import me.magikus.core.items.additions.enchants.EnchantList;
import me.magikus.core.stats.Stat;
import me.magikus.core.stats.StatBonus;
import me.magikus.core.stats.StatList;
import me.magikus.core.stats.StatType;
import me.magikus.core.tools.util.DataUtils;
import me.magikus.core.tools.util.NumberUtils;
import me.magikus.core.tools.util.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

import static me.magikus.core.tools.util.NumberUtils.getIntegerStringOf;

public class LoreBuilder {

    private final List<String> lore = new ArrayList<>();
    private final List<String> statLore = new ArrayList<>();
    private final List<String> abilityLore = new ArrayList<>();
    private final List<String> statAbilityLore = new ArrayList<>();
    private final List<String> enchantLore = new ArrayList<>();
    private String description = "";
    private Ascension ascension;
    private Rarity r = Rarity.NORMAL;
    private ItemType type = ItemType.ITEM;

    public LoreBuilder setAscension(String r) {
        ascension = AscensionManager.getAscension(r);
        return this;
    }

    public LoreBuilder setEnchants(EnchantList list) {
        if (list.asList().length < 5) {
            for (Enchant e : list.asList()) {
                String color = e.isUltra() ? e.enchantColor() + "" + ChatColor.BOLD : "" + e.enchantColor();
                enchantLore.add(color + e.displayName() + " " + NumberUtils.toRoman(e.level()));
                for (String s : TextUtils.wrap(TextUtils.getHighlightedText(e.description()), 30)) {
                    enchantLore.add(ChatColor.GRAY + s);
                }
            }
        } else {
            StringBuilder description = new StringBuilder();
            for (Enchant e : list.asList()) {
                String color = e.isUltra() ? e.enchantColor() + "" + ChatColor.BOLD : "" + e.enchantColor();
                description.append(color).append(e.displayName()).append(" ").append(NumberUtils.toRoman(e.level())).append(", ");
            }
            String strDesc = description.toString();
            strDesc = strDesc.substring(0, strDesc.length() - 2);
            enchantLore.addAll(TextUtils.wrap(strDesc, 30));
        }
        return this;
    }

    public LoreBuilder setDescription(String description) {
        this.description = TextUtils.getHighlightedText(description);
        return this;
    }

    public LoreBuilder addAbilities(AbilityList abilities) {
        for (Ability a : abilities.asList()) {
            String desc = a.description();
            List<String> split = new ArrayList<>(TextUtils.wrap(TextUtils.getHighlightedText(desc), 30));
            abilityLore.add("");
            if (a.type().weapon()) {
                if (!Objects.equals(a.type().toString(), "")) {
                    abilityLore.add(ChatColor.BLUE + "" + ChatColor.BOLD + a + " " + ChatColor.LIGHT_PURPLE + "Ability: " + a.name());
                }
                for (String s : split) {
                    abilityLore.add(ChatColor.GRAY + s);
                }
                if (a.manaCost() > 0) {
                    abilityLore.add(ChatColor.DARK_GRAY + "Cost: " + ChatColor.DARK_AQUA + a.manaCost() + " Mana " + StatType.MANA.symbol());
                }
                if (a.cooldown() > 0) {
                    abilityLore.add(ChatColor.DARK_GRAY + "Cooldown: " + ChatColor.RED + a.cooldown() + "s");
                }
            } else {
                if (a.type() == AbilityType.PASSIVE) {
                    abilityLore.add(ChatColor.LIGHT_PURPLE + "Ability: " + a.name());
                    for (String s : split) {
                        abilityLore.add(ChatColor.GRAY + s);
                    }
                } else {
                    abilityLore.add(ChatColor.LIGHT_PURPLE + (a.type() == AbilityType.FULL_SET_BONUS ? "Set Bonus: " : "Item Bonus: ") + a.name());
                    for (String s : split) {
                        abilityLore.add(ChatColor.GRAY + s);
                    }
                }
            }
        }
        return this;
    }

    //Must be called AFTER setAscension or will bug out
    public LoreBuilder setStats(StatList stats, AdditionList additions, Rarity rarity, Player player) {
        if (ascension != null) {
            setStats(stats, additions, DataUtils.getStatsOfBest(ascension.id(), rarity), ascension.displayName(), player);
        } else {
            setStats(stats, additions, null, null, player);
        }
        return this;
    }

    //Automatically called on setstats. No need to call again
    public void setStatLore(AdditionList additions, Player player) {
        if (additions != null) {
            if (additions.accepting() != ModifierType.STAT) {
                throw new IllegalArgumentException("Additions argument is of wrong type (not of type STAT)");
            }
            List<Addition> additionList = additions.asArrayList();
            for (Addition a : additionList) {
                statAbilityLore.add("");
                IStatAddition isa = (IStatAddition) a;
                String lore = isa.getLore(player);
                if (lore != null) {
                    List<String> formatted = TextUtils.wrap(TextUtils.getHighlightedText(lore), 30);
                    for (String s : formatted) {
                        statAbilityLore.add(ChatColor.GRAY + s);
                    }
                }
            }
        }
    }

    public String getStatWithBonusLineLore(Stat s, Stat[] ascensionAdditions, AdditionList additions, TreeMap<StatType, HashMap<AdditionInfo, Double>> realSorted, String ascensionName, List<StatBonus> bonuses, boolean bold) {
        StatType type = s.type();
        StringBuilder stat = new StringBuilder();
        stat.append(type.color()).append(bold ? ChatColor.BOLD : "").append(type.displayName()).append(" ").append(type.symbol()).append(": ").append(type.color()).append(bold ? ChatColor.BOLD : "").append("+").append(getIntegerStringOf(s.value(), 1)).append(type.percent() ? "%" : "");
        if (additions != null) {
            if (realSorted.containsKey(s.type())) {
                HashMap<AdditionInfo, Double> sortedValue = realSorted.get(s.type());
                for (AdditionInfo t : sortedValue.keySet()) {
                    if (t.bt() == null || t.shownLevel() == null || t.color() == null) {
                        continue;
                    }
                    Double amount = sortedValue.get(t);
                    stat.append(" ").append(t.color()).append(bold ? ChatColor.BOLD : "").append(t.bt().start()).append(amount > -1 ? "+" : "-").append(getIntegerStringOf(amount, 1)).append(type.percent() ? "%" : "").append(t.bt().end());
                }
            }
        }
        if (ascensionAdditions != null) {
            for (Stat ascensionStat : ascensionAdditions) {
                if (ascensionStat.type() == type) {
                    stat.append(" ").append(ChatColor.GOLD).append(bold ? ChatColor.BOLD : "").append("(").append(ascensionName).append(" +").append(getIntegerStringOf(ascensionStat.value(), 1)).append(")");
                }
            }
        }
        StatBonus b = null;
        for (StatBonus bonus : bonuses) {
            if (bonus.t() == s.type()) {
                b = bonus;
                break;
            }
        }
        if (b != null) {
            stat.append(" ").append(ChatColor.WHITE).append(bold ? ChatColor.BOLD : "").append("| +").append(b.bonus() * 100).append("% |");
        }
        return stat.toString();
    }

    public LoreBuilder setStats(StatList stats, AdditionList additions, StatList ascensionBonus, String ascensionName, Player player) {
        Map<StatType, HashMap<AdditionInfo, Double>> sorted;
        TreeMap<StatType, HashMap<AdditionInfo, Double>> realSorted = null;
        if (additions != null) {
            if (additions.accepting() != ModifierType.STAT) {
                throw new IllegalArgumentException("Additions argument is of wrong type (not of type STAT)");
            }
            setStatLore(additions, player);
            List<Addition> additionList = additions.asArrayList();
            sorted = new HashMap<>();
            for (Addition a : additionList) {
                IStatAddition isa = (IStatAddition) a;
                if (isa.show()) {
                    for (Stat s : isa.getStats(player, null).getStatWithBonusList()) {
                        if (sorted.containsKey(s.type())) {
                            sorted.get(s.type()).put(a.type(), s.value());
                        } else {
                            sorted.put(s.type(), new HashMap<>());
                            sorted.get(s.type()).put(a.type(), s.value());
                        }
                    }
                }
            }
            realSorted = new TreeMap<>(Comparator.comparingInt(StatType::level));
            realSorted.putAll(sorted);
            realSorted.remove(StatType.SPEED_CAP);
            realSorted.remove(StatType.HEALTH);
            realSorted.remove(StatType.MANA);
            realSorted.remove(StatType.DAMAGE_REDUCTION);
        }
        Stat[] ascensionAdditions = null;
        if (ascensionBonus != null && ascensionName != null) {
            ascensionAdditions = ascensionBonus.getStatWithBonusList();
        }
        List<Stat> statsListed = new ArrayList<>(stats.statList.values());
        List<Stat> combatStatsListed = new ArrayList<>();
        for (Stat s : statsListed) {
            if (s.type().toString().toLowerCase().contains("defense") || s.type().toString().toLowerCase().contains("damage")) {
                if (!s.type().toString().equalsIgnoreCase("crit_damage") && !s.type().toString().equalsIgnoreCase("damage_reduction")) {
                    combatStatsListed.add(s);
                }
            }
        }
        for (Stat s : combatStatsListed) {
            statsListed.remove(s);
        }
        List<StatBonus> bonusesListed = new ArrayList<>(stats.bonusList.values());
        Comparator<Stat> compareByType = Comparator.comparingInt(o -> (o.type().level()));
        statsListed.sort(compareByType);
        statLore.clear();
        for (Stat s : combatStatsListed) {
            statLore.add(getStatWithBonusLineLore(s, ascensionAdditions, additions, realSorted, ascensionName, bonusesListed, true));
        }
        if (statLore.size() > 0 && statsListed.size() > 0) {
            statLore.add("");
        }
        for (Stat s : statsListed) {
            StatType type = s.type();
            if (
                    type == StatType.SPEED_CAP ||
                            type == StatType.HEALTH ||
                            type == StatType.MANA ||
                            type == StatType.DAMAGE_REDUCTION
            ) {
                continue;
            }
            statLore.add(getStatWithBonusLineLore(s, ascensionAdditions, additions, realSorted, ascensionName, bonusesListed, false));
        }
        return this;
    }

    public LoreBuilder setRarity(Rarity r) {
        this.r = r;
        return this;
    }

    public LoreBuilder setType(ItemType type) {
        this.type = type;
        return this;
    }

    public List<String> build() {
        if (ascension != null) {
            lore.add(ChatColor.GOLD + "" + ChatColor.MAGIC + "|" + ChatColor.GOLD + ascension.displayName() + " Ascension" + ChatColor.MAGIC + "|");
            lore.add("");
        }
        if (statLore.size() > 0) {
            lore.addAll(statLore);
            lore.add("");
        }
        if (enchantLore.size() > 0) {
            lore.addAll(enchantLore);
            lore.add("");
        }
        boolean hasDescription = false;
        if (!Objects.equals(description, "")) {
            hasDescription = true;
            List<String> desc = TextUtils.wrap(description, 30);
            for (String s : desc) {
                lore.add(ChatColor.GRAY + s);
            }
        }
        if (statAbilityLore.size() > 0) {
            if (!hasDescription && statLore.size() > 0) {
                lore.remove(lore.size() - 1);
            }
            lore.addAll(statAbilityLore);
        } else {
            if (lore.size() > 0 && hasDescription && abilityLore.size() < 1) {
                lore.add("");
            }
        }
        if (abilityLore.size() > 0) {
            if (!hasDescription && statLore.size() > 0 && statAbilityLore.size() < 1) {
                lore.remove(lore.size() - 1);
            }
            lore.addAll(abilityLore);
            lore.add("");
        } else {
            if (lore.size() > 0 && statAbilityLore.size() > 0) {
                lore.add("");
            }
        }
        if (ascension != null && ascension.bonusDesc() != null) {
            lore.add(ChatColor.BLUE + ascension.displayName() + " Bonus");
            List<String> ascensionText = TextUtils.wrap(TextUtils.getHighlightedText(ascension.bonusDesc()), 30);
            for (String s : ascensionText) {
                lore.add(ChatColor.GRAY + s);
            }
            lore.add("");
        }
        if (type.reforgable() && ascension == null) {
            lore.add(ChatColor.DARK_GRAY + "This item can be ascended!");
        }
        lore.add(r.color() + "" + ChatColor.BOLD + r + " " + type.display().toUpperCase());
        return lore;
    }

}
