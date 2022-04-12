package me.magikus.additions.ascensions;

import me.magikus.core.enums.Rarity;
import me.magikus.core.items.additions.AppliableTypeDefaults;
import me.magikus.core.items.additions.ascensions.Ascension;
import me.magikus.core.stats.Stat;
import me.magikus.core.stats.StatList;
import me.magikus.core.stats.StatType;
import me.magikus.core.util.DataUtils;
import me.magikus.core.util.classes.Pair;

public class EnlightenedAscension extends Ascension {
    public EnlightenedAscension() {
        super("enlightened", "Enlightened", "Increases your Wind Damage by @@2@@ for every level", DataUtils.getStatWithBonusModifiersFrom(
                new Pair<>(Rarity.NORMAL, new StatList(new Stat(StatType.MAX_MANA, 10))),
                new Pair<>(Rarity.UNCOMMON, new StatList(new Stat(StatType.MAX_MANA, 20))),
                new Pair<>(Rarity.RARE, new StatList(new Stat(StatType.MAX_MANA, 50))),
                new Pair<>(Rarity.EPIC, new StatList(new Stat(StatType.MAX_MANA, 100))),
                new Pair<>(Rarity.LEGENDARY, new StatList(new Stat(StatType.MAX_MANA, 200))),
                new Pair<>(Rarity.RENOWNED, new StatList(new Stat(StatType.MAX_MANA, 275)))
        ), AppliableTypeDefaults.meleeWeapons);
    }
}
