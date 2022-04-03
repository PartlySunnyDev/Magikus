package me.magikus.additions.ascensions;

import me.magikus.core.enums.Rarity;
import me.magikus.core.items.ItemType;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.items.additions.AppliableTypeDefaults;
import me.magikus.core.items.additions.ascensions.Ascension;
import me.magikus.core.stats.Stat;
import me.magikus.core.stats.StatList;
import me.magikus.core.stats.StatType;
import me.magikus.core.util.DataUtils;
import me.magikus.core.util.classes.Pair;

import java.util.HashMap;

public class EnlightenedAscension extends Ascension {
    public EnlightenedAscension() {
        super("enlightened", "Enlightened", "Increases your Wind Damage by @@2@@ for every level", DataUtils.getStatModifiersFrom(
                new Pair<>(Rarity.COMMON, new StatList(new Stat(StatType.INTELLIGENCE, 10))),
                new Pair<>(Rarity.UNCOMMON, new StatList(new Stat(StatType.INTELLIGENCE, 20))),
                new Pair<>(Rarity.RARE, new StatList(new Stat(StatType.INTELLIGENCE, 50))),
                new Pair<>(Rarity.EPIC, new StatList(new Stat(StatType.INTELLIGENCE, 100))),
                new Pair<>(Rarity.LEGENDARY, new StatList(new Stat(StatType.INTELLIGENCE, 200))),
                new Pair<>(Rarity.MYTHIC, new StatList(new Stat(StatType.INTELLIGENCE, 275)))
        ), AppliableTypeDefaults.meleeWeapons);
    }
}
