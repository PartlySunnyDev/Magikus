package me.magikus.magic.spells;

import me.magikus.core.entities.damage.DamageManager;
import me.magikus.core.entities.damage.DamageType;
import me.magikus.core.entities.damage.Element;
import me.magikus.core.entities.damage.modifiers.DamageModifier;
import me.magikus.core.magic.spells.Spell;
import me.magikus.core.player.PlayerStatManager;
import me.magikus.core.stats.StatType;
import me.magikus.core.tools.classes.Pair;
import me.magikus.core.tools.classes.Requirements;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ZapSpell extends Spell {
    public ZapSpell() {
        super("zap", "Bring the power of Thor down on your enemies!", ChatColor.YELLOW + "Zap", new Requirements(), 10);
    }

    @Override
    public void castSpell(Player p) {
        double damage = PlayerStatManager.getStatWithBonus(p.getUniqueId(), StatType.MAX_MANA) * 20;
        for (Entity e : p.getNearbyEntities(5, 5, 5)) {
            if (e instanceof LivingEntity) {
                e.getWorld().strikeLightningEffect(e.getLocation());
                DamageManager.dealDamage((LivingEntity) e, new HashMap<>() {{
                    put(Element.ELECTRIC, damage);
                }}, new Pair<>(damage, false), new DamageModifier[]{}, true, DamageType.MAGICAL);
            }
        }
    }
}
