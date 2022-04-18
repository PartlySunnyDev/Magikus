package me.magikus.abilities.rightClick;

import me.magikus.core.entities.damage.DamageManager;
import me.magikus.core.entities.damage.DamageType;
import me.magikus.core.entities.damage.Element;
import me.magikus.core.entities.damage.modifiers.DamageModifier;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.items.abilities.Ability;
import me.magikus.core.items.abilities.AbilityType;
import me.magikus.core.items.additions.AppliableTypeDefaults;
import me.magikus.core.player.PlayerStatManager;
import me.magikus.core.stats.StatType;
import me.magikus.core.tools.classes.Pair;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class SmiteAbility extends Ability {

    public SmiteAbility() {
        this(null);
    }

    public SmiteAbility(MagikusItem parent) {
        super("smite", "Smite", "Strike down enemies around you, dealing Electric Damage", 160, 1, AbilityType.RIGHT_CLICK, parent, AppliableTypeDefaults.meleeWeapons);
    }

    @Override
    protected void trigger(Player player, ItemStack parent) {
        double damage = PlayerStatManager.getStatWithBonus(player.getUniqueId(), StatType.STRENGTH) * 5;
        for (Entity e : player.getNearbyEntities(5, 5, 5)) {
            if (e instanceof LivingEntity) {
                e.getWorld().strikeLightningEffect(e.getLocation());
                DamageManager.dealDamage((LivingEntity) e, new HashMap<>() {{
                    put(Element.ELECTRIC, damage);
                }}, new Pair<>(damage, false), new DamageModifier[]{}, true, DamageType.MAGICAL);
            }
        }
    }
}
