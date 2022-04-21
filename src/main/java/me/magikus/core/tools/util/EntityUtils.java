package me.magikus.core.tools.util;

import com.google.common.collect.ImmutableSet;
import me.magikus.core.entities.EntityInfo;
import me.magikus.core.entities.EntityManager;
import me.magikus.core.entities.damage.Element;
import me.magikus.core.entities.stats.EntityStat;
import me.magikus.core.entities.stats.EntityStatList;
import me.magikus.core.entities.stats.EntityStatType;
import me.magikus.core.enums.VanillaEntityDamageAttributes;
import me.magikus.core.enums.VanillaEntityHealthAttributes;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

import static me.magikus.core.tools.util.TextUtils.getHealthText;

public class EntityUtils {
    public static void setBoss(byte b, Entity e) {
        DataUtils.setData("mg_boss", b, PersistentDataType.BYTE, e);
    }

    public static Boolean isBoss(Entity e) {
        Byte boss = (Byte) DataUtils.getData("mg_boss", PersistentDataType.BYTE, e);
        return boss != null ? boss == 0 : null;
    }

    public static EntityType<?> getTypeOfCustomEntity(String entityId) throws Exception {
        EntityInfo entity = EntityManager.getEntity(entityId);
        return entity.entityType();
    }

    public static EntityType<?> getNewHostileType(EntityType.EntityFactory<?> factory, boolean immuneToFire, EntityDimensions entitySize, int clientTrackingRange) {
        return new EntityType<>(factory, MobCategory.MONSTER, true, true, immuneToFire, true, ImmutableSet.of(), entitySize, clientTrackingRange, 1);
    }

    public static void setId(String s, Entity e) {
        DataUtils.setData("mg_id", s, PersistentDataType.STRING, e);
    }

    public static String getId(Entity e) {
        return (String) DataUtils.getData("mg_id", PersistentDataType.STRING, e);
    }

    public static void setIgnore(boolean s, Entity e) {
        DataUtils.setData("mg_ignore", s ? (byte) 1 : (byte) 0, PersistentDataType.BYTE, e);
    }

    public static boolean getIgnore(Entity e) {
        Object mg_ignore = DataUtils.getData("mg_ignore", PersistentDataType.BYTE, e);
        if (mg_ignore == null) {
            return false;
        }
        return ((byte) mg_ignore) == 1;
    }

    public static void setHealth(double s, Entity e) {
        DataUtils.setData("mg_health", s, PersistentDataType.DOUBLE, e);
    }

    public static Double getHealth(Entity e) {
        return (Double) DataUtils.getData("mg_health", PersistentDataType.DOUBLE, e);
    }

    public static void spawnEntity(net.minecraft.world.entity.Entity entity) {
        entity.level.addFreshEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    public static void spawnEntity(net.minecraft.world.entity.Entity entity, CreatureSpawnEvent.SpawnReason r) {
        entity.level.addFreshEntity(entity, r);
    }

    public static void setVanilla(Entity e) {
        setBoss((byte) 1, e);
        setId(e.getType().toString().toLowerCase(), e);
        for (EntityStatType s : EntityStatType.values()) {
            switch (s) {
                case SPEED -> EntityStatType.setStat(e, s, 100);
                case DAMAGE -> {
                    try {
                        VanillaEntityDamageAttributes damage = VanillaEntityDamageAttributes.valueOf(e.getType().toString());
                        EntityStatType.setStat(e, s, damage.getValue());
                    } catch (IllegalArgumentException error) {
                        EntityStatType.setStat(e, s, 0);
                    }
                }
                case MAX_HEALTH -> {
                    try {
                        VanillaEntityHealthAttributes health = VanillaEntityHealthAttributes.valueOf(e.getType().toString());
                        EntityStatType.setStat(e, s, health.getValue());
                        setHealth(health.getValue(), e);
                    } catch (IllegalArgumentException error) {
                        EntityStatType.setStat(e, s, 0);
                    }
                }
                case DEFENSE -> EntityStatType.setStat(e, s, 0);
            }
        }
    }

    public static void repairEntity(Entity e) {
        for (EntityStatType s : EntityStatType.values()) {
            if (EntityStatType.getStat(e, s) == null) {
                switch (s) {
                    case SPEED -> EntityStatType.setStat(e, s, 100);
                    case DAMAGE -> {
                        try {
                            VanillaEntityDamageAttributes damage = VanillaEntityDamageAttributes.valueOf(e.getType().toString());
                            EntityStatType.setStat(e, s, damage.getValue() * 5);
                        } catch (IllegalArgumentException error) {
                            EntityStatType.setStat(e, s, 0);
                        }
                    }
                    case MAX_HEALTH -> {
                        try {
                            VanillaEntityHealthAttributes health = VanillaEntityHealthAttributes.valueOf(e.getType().toString());
                            EntityStatType.setStat(e, s, health.getValue() * 5);
                        } catch (IllegalArgumentException error) {
                            EntityStatType.setStat(e, s, 0);
                        }
                    }
                    case DEFENSE -> EntityStatType.setStat(e, s, 0);
                }
            }
        }
        if (EntityUtils.getHealth(e) == null) {
            EntityUtils.setHealth(EntityStatType.getStat(e, EntityStatType.MAX_HEALTH), e);
        }
        if (EntityUtils.isBoss(e) == null) {
            EntityUtils.setBoss((byte) 1, e);
        }
        if (EntityUtils.getId(e) == null) {
            EntityUtils.setId(e.getType().toString().toLowerCase(), e);
        }
    }

    public static String getDisplayName(Entity e) {
        String id = getId(e);
        if (id != null) {
            EntityInfo info = EntityManager.getEntity(id);
            double hp = getHealth(e);
            if (info == null) {
                if (e instanceof LivingEntity le) {
                    //Vanilla entity
                    return ChatColor.RED + TextUtils.capitalizeWord(e.getType().toString().toLowerCase().replace('_', ' ')) + " " + ChatColor.GREEN + (getHealthText(hp) + "/" + VanillaEntityHealthAttributes.valueOf(e.getType().toString()).getValue() * 5) + ChatColor.RED + "❤ " + ChatColor.GOLD + " [" + ChatColor.GOLD + "Lv" + (int) Math.ceil(le.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() / 10) + "] ";
                } else {
                    return "";
                }
            } else {
                //Custom entity
                return info.type().color() + info.type().icon() + " " + info.color() + info.displayName() + " " + ChatColor.GREEN + (info.isBoss() ? getHealthText(hp) : getHealthText(hp) + "/" + getHealthText(info.stats().getStat("mg_max_health"))) + ChatColor.RED + "❤" + ChatColor.GOLD + " [Lv" + info.level() + "] ";
            }
        }
        return "NULL";
    }

    public static String getElementalInfo(Entity e) {
        String id = getId(e);
        if (id != null) {
            EntityInfo info = EntityManager.getEntity(id);
            if (info == null) {
                return "";
            }
            StringBuilder weak = new StringBuilder();
            StringBuilder dam = new StringBuilder();
            StringBuilder def = new StringBuilder();
            EntityStatList l = info.stats();
            for (EntityStat es : l.getStatList()) {
                String toString = es.type().toString();
                if (toString.endsWith("_DEFENSE")) {
                    Element element = Element.valueOf(toString.substring(0, toString.length() - 8));
                    if (es.value() > 0) {
                        def.append(element.color()).append(element.icon());
                    } else if (es.value() != 0) {
                        weak.append(element.color()).append(element.icon());
                    }
                } else if (toString.endsWith("_DAMAGE")) {
                    Element element = Element.valueOf(toString.substring(0, toString.length() - 7));
                    dam.append(element.color()).append(element.icon());
                }
            }
            return ChatColor.GRAY + "Dam" + dam + " " + ChatColor.GRAY + "Def" + def + " " + ChatColor.GRAY + "Weak" + weak;
        }
        return "";
    }

    public static String getName(Entity e) {
        String id = getId(e);
        EntityInfo info = EntityManager.getEntity(id);
        if (info == null) {
            return TextUtils.capitalizeWord(e.getType().toString().toLowerCase().replace('_', ' '));
        } else {
            return info.displayName();
        }
    }

    public static void setEntityInfo(Entity e, String id) {
        EntityInfo info = EntityManager.getEntity(id);
        if (info != null) {
            if (e instanceof LivingEntity) {
                Objects.requireNonNull(((LivingEntity) e).getEquipment()).setArmorContents(info.armorSlots());
                ((LivingEntity) e).getEquipment().setItemInMainHand(info.itemInMainHand());
            }
            setId(id, e);
            setBoss(info.isBoss() ? (byte) 0 : (byte) 1, e);
            setHealth(info.stats().getStat("mg_max_health"), e);
            info.stats().applyStats(e);
        }
    }

}
