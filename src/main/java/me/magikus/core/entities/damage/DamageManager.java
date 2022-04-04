package me.magikus.core.entities.damage;

import me.magikus.Magikus;
import me.magikus.core.ConsoleLogger;
import me.magikus.core.entities.EntityInfo;
import me.magikus.core.entities.EntityManager;
import me.magikus.core.entities.stats.EntityStatType;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.player.BaseStatManager;
import me.magikus.core.player.PlayerStatManager;
import me.magikus.core.player.PlayerUpdater;
import me.magikus.core.stats.StatList;
import me.magikus.core.stats.StatType;
import me.magikus.core.util.EntityUtils;
import me.magikus.core.util.NumberUtils;
import me.magikus.core.util.TextUtils;
import me.magikus.core.util.classes.Pair;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static me.magikus.core.entities.stats.EntityStatType.getStat;

public class DamageManager implements Listener {

    private static final Map<UUID, Pair<String, Long>> lastDamagedBy = new HashMap<>();

    public static void dealDamage(LivingEntity e, Map<Element, Double> elementalInfo, Pair<Double, Boolean> damageInfo, boolean showDamageIndicator) {
        if (e.getType() == EntityType.ARMOR_STAND || e.getType() == EntityType.PLAYER) {
            return;
        }
        if (EntityUtils.getHealth(e) == null) {
            EntityUtils.repairEntity(e);
        }
        EntityUtils.setHealth(EntityUtils.getHealth(e) - damageInfo.a(), e);
        if (EntityUtils.getHealth(e) < 1) {
            e.setHealth(0);
            EntityUtils.setHealth(0, e);
        }
        e.setCustomName(EntityUtils.getDisplayName(e));
        if (showDamageIndicator)
            summonDamageIndicator(e.getLocation(), elementalInfo, damageInfo, e.getHeight());
    }

    public static void dealDamage(LivingEntity e, Map<Element, Double> elementalInfo, Pair<Double, Boolean> damageInfo, boolean showDamageIndicator, Player p, boolean ferocity) {
        dealDamage(e, elementalInfo, damageInfo, showDamageIndicator);
        double ferocityChance = PlayerStatManager.getStat(p.getUniqueId(), StatType.ATTACK_EFFICIENCY);
        if (!ferocity) {
            int one = (int) ((ferocityChance) % 10);
            int tens = (int) ((ferocityChance / 10) % 10);
            int hundred = (int) (Math.floor(ferocityChance / 100));
            boolean ferocityActive = new Random().nextInt(100) < one + tens * 10;
            for (int i = 0; i < hundred; i++) {
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EGG_THROW, 0.7f, 1.5f);
                dealDamage(e, elementalInfo, damageInfo, showDamageIndicator, p, true);
            }
            if (ferocityActive) {
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EGG_THROW, 0.7f, 1.5f);
                dealDamage(e, elementalInfo, damageInfo, showDamageIndicator, p, true);
            }
            return;
        }
        double attackSpeed = PlayerStatManager.getStat(p.getUniqueId(), StatType.ATTACK_SPEED);
        ((CraftLivingEntity) e).getHandle().invulnerableDuration = (int) (10 / (1 + attackSpeed / 100));
        ((CraftLivingEntity) e).getHandle().invulnerableTime = (int) (10 / (1 + attackSpeed / 100));
    }

    public static void summonDamageIndicator(Location central, Map<Element, Double> elementalInfo, Pair<Double, Boolean> damageInfo, double entityHeight) {
        Random r = new Random();
        double xOffset = (r.nextInt(200) / 100f) - 1;
        double yOffset = (entityHeight / 2 + ((r.nextInt((int) (entityHeight * 50)) / 100f) - entityHeight / 4)) + entityHeight / 2;
        double zOffset = (r.nextInt(200) / 100f) - 1;
        ArmorStand temp = new ArmorStand(((CraftWorld) central.getWorld()).getHandle(), central.getX() + xOffset, central.getY() - 1 + yOffset, central.getZ() + zOffset);
        temp.setMarker(true);
        temp.setInvisible(true);
        temp.setNoGravity(true);
        temp.noCulling = true;
        temp.setCustomName(new TextComponent(TextUtils.getDamageText(elementalInfo, damageInfo.b())));
        temp.setCustomNameVisible(true);
        EntityUtils.spawnEntity(temp);
        new BukkitRunnable() {
            @Override
            public void run() {
                //To prevent stupid noise
                temp.teleportTo(temp.getX(), -50, temp.getY());
                temp.kill();
            }
        }.runTaskLater(JavaPlugin.getPlugin(Magikus.class), 35 + new Random().nextInt(10));
    }

    public static String getCritText(String before) {
        StringBuilder temp = new StringBuilder();
        temp.append(ChatColor.WHITE).append("✧");
        ChatColor[] cycle = new ChatColor[]{
            ChatColor.WHITE,
            ChatColor.YELLOW,
            ChatColor.GOLD,
            ChatColor.RED,
        };
        int count = 0;
        for (char c : before.toCharArray()) {
            if (before.length() > 2) {
                if (count > 3) {
                    count = 0;
                }
                temp.append(cycle[count]).append(c);
            } else {
                temp.append(cycle[3 - count]).append(c);
            }
            count++;
        }
        temp.append(ChatColor.WHITE).append("✧");
        return temp.toString();
    }

    public static Pair<Map<Element, Double>, Pair<Double, Boolean>> getHitDamage(Player p, Entity e, boolean ignoreHand) {
        ItemStack itemInMainHand = p.getInventory().getItemInMainHand();
        if (itemInMainHand.hasItemMeta()) {
            MagikusItem itemFrom = MagikusItem.getItemFrom(itemInMainHand, p);
            if (itemFrom == null) {
                ConsoleLogger.console("Invalid item in main hand");
                return null;
            }
            DamageType t = itemFrom.damageType();
            return PlayerUpdater.getStats(p, ignoreHand, e).getFinalDamage(e, ignoreHand, t);
        } else {
            return PlayerUpdater.getStats(p, ignoreHand, e).getFinalDamage(e, ignoreHand, DamageType.PHYSICAL);
        }
    }

    public static double getHitDamageOn(Player p, Entity e, double rawDamage, boolean trueDamage, Element element) {
        StatList stats = PlayerUpdater.getStats(p, false, e);
        if (trueDamage) {
            return rawDamage;
        }
        double fireDefense = (stats.getStat(StatType.FIRE_DEFENSE));
        double waterDefense = (stats.getStat(StatType.WATER_DEFENSE));
        double windDefense = (stats.getStat(StatType.WIND_DEFENSE));
        double electricDefense = (stats.getStat(StatType.ELECTRIC_DEFENSE));
        double earthDefense = (stats.getStat(StatType.EARTH_DEFENSE));
        double iceDefense = (stats.getStat(StatType.ICE_DEFENSE));
        double regularDefense = (stats.getStat(StatType.DEFENSE));
        double reduction = stats.getStat(StatType.DAMAGE_REDUCTION);
        switch (element) {
            case ICE -> rawDamage -= (((reduction / 100) * rawDamage) + ((iceDefense / (iceDefense + 100)) * rawDamage));
            case WATER -> rawDamage -= (((reduction / 100) * rawDamage) + ((waterDefense / (waterDefense + 100)) * rawDamage));
            case WIND -> rawDamage -= (((reduction / 100) * rawDamage) + ((windDefense / (windDefense + 100)) * rawDamage));
            case ELECTRIC -> rawDamage -= (((reduction / 100) * rawDamage) + ((electricDefense / (electricDefense + 100)) * rawDamage));
            case FIRE -> rawDamage -= (((reduction / 100) * rawDamage) + ((fireDefense / (fireDefense + 100)) * rawDamage));
            case EARTH -> rawDamage -= (((reduction / 100) * rawDamage) + ((earthDefense / (earthDefense + 100)) * rawDamage));
        }
        double reducedDamage = rawDamage - ((regularDefense / (regularDefense + 100)) * rawDamage);
        if (reducedDamage < 0) {
            return 0;
        }
        return reducedDamage;
    }

    public static void damagePlayer(Player p, double damage) {
        damagePlayer(p, damage, null, "");
    }

    public static void damagePlayer(Player p, double damage, EntityDamageEvent.DamageCause cause, String entityName) {
        UUID uniqueId = p.getUniqueId();
        PlayerStatManager.changeStat(uniqueId, StatType.HEALTH, -damage);
        double health = PlayerStatManager.getStat(uniqueId, StatType.HEALTH);
        double maxHealth = PlayerStatManager.getStat(uniqueId, StatType.MAX_HEALTH);
        if (!entityName.equals("")) {
            lastDamagedBy.put(p.getUniqueId(), new Pair<>(entityName, Instant.now().getEpochSecond()));
        }
        updatePlayerHealthBar(p, health, maxHealth, cause);
    }

    public static void updatePlayerHealthBar(Player p, double mgHp, double mgMaxHp) {
        updatePlayerHealthBar(p, mgHp, mgMaxHp, null);
    }

    public static void updatePlayerHealthBar(Player p, double mgHp, double mgMaxHp, EntityDamageEvent.DamageCause cause) {
        if (mgHp < 1) {
            Pair<String, Long> info = lastDamagedBy.get(p.getUniqueId());
            if (info == null) {
                kill(p, cause, "");
            } else {
                String killer = info.a();
                if (Instant.now().getEpochSecond() - info.b() > 3) {
                    killer = "";
                }
                kill(p, cause, killer);
            }
        } else {
            double newHp = 20;
            if (mgMaxHp < 200) {
                p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
            } else {
                p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20 + NumberUtils.clamp((int) (mgMaxHp / 50f), 0, 20));
            }
            if (mgHp - 100 < 0) {
                newHp = mgHp / 5;
            } else {
                newHp += NumberUtils.clamp((int) (mgHp / 50f), 0, 20);
            }
            setHealth(p, newHp);
        }
    }

    public static void kill(Player p, EntityDamageEvent.DamageCause cause, String killer) {
        BaseStatManager.initializeStats(p);
        Location bedSpawnLocation = p.getBedSpawnLocation();
        if (bedSpawnLocation == null) {
            bedSpawnLocation = p.getWorld().getSpawnLocation();
        }
        p.teleport(bedSpawnLocation);
        p.setFireTicks(0);
        p.setVisualFire(false);
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 1);
        if (cause == null) {
            for (Player a : p.getWorld().getPlayers()) {
                a.sendMessage(ChatColor.RED + "☠ " + a.getDisplayName() + " died.");
            }
        } else {
            for (Player a : p.getWorld().getPlayers()) {
                switch (cause) {
                    case FALL -> a.sendMessage(ChatColor.RED + "☠ " + ChatColor.GRAY + p.getDisplayName() + " fell to their death" + (killer.equals("") ? "." : " with help from " + killer + "."));
                    case FIRE, FIRE_TICK -> a.sendMessage(ChatColor.RED + "☠ " + ChatColor.GRAY + p.getDisplayName() + " burnt to death" + (killer.equals("") ? "." : " while trying to fight " + killer + "."));
                    case VOID -> a.sendMessage(ChatColor.RED + "☠ " + ChatColor.GRAY + p.getDisplayName() + (!killer.equals("") ? " was thrown into the void by " + killer : " fell into the void"));
                    case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK, ENTITY_EXPLOSION, MAGIC -> a.sendMessage(ChatColor.RED + "☠ " + ChatColor.GRAY + p.getDisplayName() + " was slain by " + killer + ".");
                    case SUFFOCATION -> a.sendMessage(ChatColor.RED + "☠ " + ChatColor.GRAY + p.getDisplayName() + " suffocated.");
                    case DROWNING -> a.sendMessage(ChatColor.RED + "☠ " + ChatColor.GRAY + p.getDisplayName() + " drowned.");
                    case BLOCK_EXPLOSION -> a.sendMessage(ChatColor.RED + "☠ " + ChatColor.GRAY + p.getDisplayName() + " blew up.");
                    default -> a.sendMessage(ChatColor.RED + "☠ " + ChatColor.GRAY + p.getDisplayName() + " died");
                }
            }
        }
        lastDamagedBy.remove(p.getUniqueId());
    }

    public static void setHealth(Player p, double health) {
        net.minecraft.world.entity.player.Player player = ((CraftPlayer) p).getHandle();
        player.setHealth((float) health);
    }

    @EventHandler
    public void onHungerDeplete(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player p) {
            e.setCancelled(true);
            p.setFoodLevel(20);
            p.setSaturation(0);
        }
    }

    @EventHandler
    public void entityAttack(EntityDamageByEntityEvent e) {
        if (e.getDamage() <= 0) {
            return;
        }
        if (!(e.getDamager() instanceof LivingEntity damager) || e.getDamager() instanceof Player || !(e.getEntity() instanceof LivingEntity receiver) || e.getDamager().getType() == EntityType.ARROW || e.getDamager().getType() == EntityType.SPECTRAL_ARROW) {
            return;
        }
        e.setDamage(0);
        if (!(damager instanceof Player)) {
            if (receiver instanceof Player) {
                damagePlayer((Player) receiver, getHitDamageOn((Player) receiver, damager, EntityStatType.getStat(damager, EntityStatType.DAMAGE), false, EntityManager.getEntity(EntityUtils.getId(damager)) != null ? EntityManager.getEntity(EntityUtils.getId(damager)).type() : Element.NONE), EntityDamageEvent.DamageCause.ENTITY_ATTACK, EntityUtils.getName(damager));
            } else {
                dealDamage(receiver, new HashMap<>() {{
                    put(Element.NONE, getStat(damager, EntityStatType.DAMAGE));
                }}, new Pair<>(getStat(damager, EntityStatType.DAMAGE), false), false);
            }
        }
    }

    @EventHandler
    public void playerAttack(EntityDamageByEntityEvent e) {
        if (e.getDamage() <= 0) {
            return;
        }
        if (!(e.getDamager() instanceof Player p) || !(e.getEntity() instanceof LivingEntity receiver) || e.getDamager().getType() == EntityType.ARROW || e.getDamager().getType() == EntityType.SPECTRAL_ARROW) {
            return;
        }
        e.setDamage(0);
        if (receiver instanceof Player) {
            e.setCancelled(true);
        } else {
            if ((p).getInventory().getItemInMainHand().getType() == Material.CROSSBOW || p.getInventory().getItemInMainHand().getType() == Material.BOW) {
                Pair<Map<Element, Double>, Pair<Double, Boolean>> hitDamage = getHitDamage(p, receiver, true);
                dealDamage(receiver, hitDamage.a(), hitDamage.b(), true, p, false);
                return;
            }
            Pair<Map<Element, Double>, Pair<Double, Boolean>> hitDamage = getHitDamage(p, receiver, false);
            dealDamage(receiver, hitDamage.a(), hitDamage.b(), true, p, false);
        }
    }

    @EventHandler
    public void entityArrowAttack(ProjectileHitEvent e) {
        if (!(e.getEntity().getShooter() instanceof LivingEntity damager) || !(e.getHitEntity() instanceof LivingEntity receiver)) {
            return;
        }
        receiver.damage(0, damager);
        if (!(damager instanceof Player)) {
            if (receiver instanceof Player) {
                damagePlayer((Player) receiver, getHitDamageOn((Player) receiver, damager, EntityStatType.getStat(damager, EntityStatType.DAMAGE), false, EntityManager.getEntity(EntityUtils.getId(damager)) != null ? EntityManager.getEntity(EntityUtils.getId(damager)).type() : Element.NONE), EntityDamageEvent.DamageCause.ENTITY_ATTACK, EntityUtils.getName(damager));
            } else {
                dealDamage(receiver, new HashMap<>() {{
                    put(Element.NONE, getStat(damager, EntityStatType.DAMAGE));
                }}, new Pair<>(getStat(damager, EntityStatType.DAMAGE), false), false);
            }
        }
        e.getEntity().remove();
        e.setCancelled(true);
    }

    @EventHandler
    public void playerArrowAttack(ProjectileHitEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player p) || !(e.getHitEntity() instanceof LivingEntity receiver)) {
            return;
        }
        receiver.damage(0, p);
        if (receiver instanceof Player) {
            e.setCancelled(true);
        } else {
            Pair<Map<Element, Double>, Pair<Double, Boolean>> hitDamage = getHitDamage(p, receiver, false);
            dealDamage(receiver, hitDamage.a(), hitDamage.b(), true, p, false);
        }
        e.getEntity().remove();
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void naturalDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof LivingEntity &&
            e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK &&
            e.getCause() != EntityDamageEvent.DamageCause.PROJECTILE &&
            e.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK &&
            e.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
        ) {
            if (e.getEntity() instanceof Player) {
                damagePlayer((Player) e.getEntity(), getHitDamageOn((Player) e.getEntity(), null, e.getDamage() * 5, false, Element.NONE), e.getCause(), "");
            } else {
                dealDamage((LivingEntity) e.getEntity(), new HashMap<>() {{
                    put(Element.NONE, e.getDamage() * 5);
                }}, new Pair<>(e.getDamage() * 5, false), true);
            }
            e.setDamage(0);
        }
    }

}
