package me.magikus.abilities.rightClick;

import me.magikus.core.ConsoleLogger;
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
import me.magikus.core.tools.util.NumberUtils;
import me.magikus.core.tools.util.TextUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WitherImpactAbility extends Ability {

    public WitherImpactAbility() {
        this(null);
    }

    public WitherImpactAbility(MagikusItem parent) {
        super("witherimpact", "Wither Impact", TextUtils.getHighlightedText("Teleports %%10 blocks%% in front of you. Then implode dealing @@10000@@ damage to nearby enemies. Also applies the " + ChatColor.GOLD + "wither shield" + ChatColor.GRAY + " ability reducing damage taken and granting a shield for 5 seconds"), 300, 0, AbilityType.RIGHT_CLICK, parent, ChatColor.RED + "This ability is on cooldown!", AppliableTypeDefaults.meleeWeapons);
    }

    @Override
    protected void trigger(Player player, ItemStack parent) {
        Vector v = player.getLocation().toVector();
        Vector backupV = player.getLocation().toVector();
        Vector d = player.getLocation().getDirection();
        NumberUtils.checkNormalizeSafe(d);
        d.normalize();
        Block toBlock = player.getTargetBlock(null, 10);
        Location location;
        if (toBlock.isEmpty()) {
            location = toBlock.getLocation();
        } else {
            location = toBlock.getLocation().toVector().subtract(d).toLocation(player.getWorld());
        }
        location.setPitch(player.getLocation().getPitch());
        location.setYaw(player.getLocation().getYaw());
        player.teleport(location);
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 25, 2, 2, 2);
        player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation(), 2, 2, 2, 2);
        player.getWorld().spawnParticle(Particle.ASH, player.getLocation(), 25, 2, 2, 2);
        for (int i = 0; i < 10; i++) {
            World world = player.getWorld();
            world.spawnParticle(Particle.ASH, backupV.toLocation(world), 10, 0.5, 0.5, 0.5);
            Collection<Entity> closeBy = world.getNearbyEntities(backupV.toLocation(world), 2, 2, 2);
            for (Entity e : closeBy) {
                if (e instanceof LivingEntity le) {
                    DamageManager.dealDamage(le, new HashMap<>() {{put(Element.NONE, 10000D);}}, new Pair<>(10000D, false), new DamageModifier[]{}, true, DamageType.MAGICAL);
                }
            }
            backupV.add(d);
        }
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        PlayerStatManager.setStat(player.getUniqueId(), StatType.HEALTH, PlayerStatManager.getStatWithBonus(player.getUniqueId(), StatType.MAX_HEALTH));
    }
}
