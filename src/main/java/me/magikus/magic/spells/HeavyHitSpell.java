package me.magikus.magic.spells;

import me.magikus.core.entities.damage.DamageManager;
import me.magikus.core.entities.damage.DamageType;
import me.magikus.core.entities.damage.Element;
import me.magikus.core.entities.damage.modifiers.DamageModifier;
import me.magikus.core.magic.spells.Spell;
import me.magikus.core.player.PlayerStatManager;
import me.magikus.core.tools.classes.Pair;
import me.magikus.core.tools.classes.Requirements;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class HeavyHitSpell extends Spell {
    public HeavyHitSpell() {
        super("heavyhit", "Smack entities around you, dealing a lot of damage", ChatColor.GOLD + "Heavy Hit", new Requirements(), 50);
    }

    @Override
    public void castSpell(Player p) {
        for (Entity e : p.getNearbyEntities(2, 2, 2)) {
            if (e instanceof LivingEntity) {
                Pair<Map<Element, Double>, Pair<Double, Boolean>> damage = PlayerStatManager.playerStats.get(p.getUniqueId()).getFinalDamage(e, false, DamageType.PHYSICAL, 5);
                DamageManager.dealDamage((LivingEntity) e, damage.a(), damage.b(), new DamageModifier[]{DamageModifier.STRONG}, true, DamageType.PHYSICAL);
                e.playEffect(EntityEffect.HURT);
            }
        }
        p.getWorld().spawnParticle(Particle.CRIT, p.getLocation().add(0, 1, 0), 2, 2, 2, 30);
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 2, 0.8f);
    }
}
