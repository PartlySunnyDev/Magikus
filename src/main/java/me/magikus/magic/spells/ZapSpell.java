package me.magikus.magic.spells;

import me.magikus.core.entities.damage.DamageManager;
import me.magikus.core.entities.damage.Element;
import me.magikus.core.magic.spells.Spell;
import me.magikus.core.player.PlayerStatManager;
import me.magikus.core.stats.StatType;
import me.magikus.core.util.Requirements;
import me.magikus.core.util.classes.Pair;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ZapSpell extends Spell {
    public ZapSpell() {
        super("zap", "Zap", new Requirements(), 10);
    }

    @Override
    public void castSpell(Player p) {
        double damage = PlayerStatManager.getStat(p.getUniqueId(), StatType.INTELLIGENCE) * 20;
        for (Entity e : p.getNearbyEntities(5, 5, 5)) {
            if (e instanceof LivingEntity) {
                e.getWorld().strikeLightningEffect(e.getLocation());
                DamageManager.dealDamage((LivingEntity) e, new HashMap<>() {{
                    put(Element.ELECTRIC, damage);
                }}, new Pair<>(damage, false), true);
            }
        }
    }
}
