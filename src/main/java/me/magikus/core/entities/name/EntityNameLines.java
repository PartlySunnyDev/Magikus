package me.magikus.core.entities.name;

import me.magikus.core.tools.util.DataUtils;
import me.magikus.core.tools.util.EntityUtils;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class EntityNameLines {

    public final Entity parent;
    private final List<String> lines = new ArrayList<>();
    private final List<ArmorStand> armorStands = new ArrayList<>();

    public EntityNameLines(Entity parent, String... lines) {
        this.lines.addAll(List.of(lines));
        this.parent = parent;
        update();
    }

    public String getLine(int index) {
        if (index > lines.size() - 2) {
            return "";
        }
        return lines.get(index);
    }

    public void setLine(int index, String value) {
        if (index >= lines.size()) {
            for (int i = 0; i < (index + 1) - lines.size(); i++) {
                lines.add("");
            }
        }
        lines.set(index, value);
        update();
    }

    public void removeLine(int index) {
        if (index >= lines.size()) {
            return;
        }
        lines.remove(index);
        update();
    }

    public void killAllStands() {
        for (ArmorStand a : armorStands) {
            a.setSilent(true);
            a.remove();
        }
        armorStands.clear();
    }

    public void update() {
        if (parent.isDead() || !parent.isValid()) {
            killAllStands();
            EntityNameManager.entityNames.remove(parent.getUniqueId());
            return;
        }
        if (!(parent instanceof LivingEntity)) {
            killAllStands();
            EntityNameManager.entityNames.remove(parent.getUniqueId());
            return;
        }
        boolean anyDead = false;
        for (ArmorStand a : armorStands) {
            if (a.isDead()) {
                anyDead = true;
                break;
            }
        }
        Location location = ((LivingEntity) parent).getEyeLocation();
        if (armorStands.size() != lines.size() || anyDead) {
            killAllStands();
            int count = 0;
            for (String l : lines) {
                net.minecraft.world.entity.decoration.ArmorStand nmsStand = new net.minecraft.world.entity.decoration.ArmorStand(net.minecraft.world.entity.EntityType.ARMOR_STAND, ((CraftWorld) parent.getWorld()).getHandle());
                ArmorStand a = (ArmorStand) nmsStand.getBukkitEntity();
                DataUtils.setData("nametagId", parent.getUniqueId().toString(), PersistentDataType.STRING, parent);
                a.setGravity(false);
                a.setMarker(true);
                a.setInvisible(true);
                a.setCustomName(l);
                a.setCustomNameVisible(true);
                a.teleport(new Location(parent.getWorld(), location.getX(), location.getY() + 0.25 + ((lines.size() - 1 - count) * 0.3), location.getZ()));
                armorStands.add(a);
                EntityUtils.spawnEntity(nmsStand);
                count++;
            }
            return;
        }
        for (int i = 0; i < armorStands.size(); i++) {
            ArmorStand armorStand = armorStands.get(i);
            armorStand.teleport(new Location(parent.getWorld(), location.getX(), location.getY() + 0.25 + ((lines.size() - 1 - i) * 0.3), location.getZ()));
            armorStand.setCustomName(lines.get(i));
        }
    }

}
