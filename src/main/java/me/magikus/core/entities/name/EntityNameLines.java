package me.magikus.core.entities.name;

import me.magikus.core.tools.util.EntityUtils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class EntityNameLines {

    private final List<String> lines = new ArrayList<>();
    private final List<ArmorStand> armorStands = new ArrayList<>();
    public final Entity parent;

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
            a.teleportTo(a.getX(), -50, a.getZ());
            a.kill();
        }
        armorStands.clear();
    }

    public void update() {
        if (parent.isDead() || !parent.isValid()) {
            killAllStands();
            return;
        }
        if (!(parent instanceof LivingEntity)) {
            killAllStands();
            return;
        }
        boolean anyDead = false;
        for (ArmorStand a : armorStands) {
            if (!a.valid) {
                anyDead = true;
                break;
            }
        }
        Level level = ((CraftWorld) parent.getWorld()).getHandle();
        Location location = ((LivingEntity) parent).getEyeLocation();
        if (armorStands.size() != lines.size() || anyDead) {
            for (ArmorStand a : armorStands) {
                a.teleportTo(a.getX(), -50, a.getZ());
                a.kill();
            }
            armorStands.clear();
            int count = 0;
            for (String l : lines) {
                ArmorStand as = new ArmorStand(level, location.getX(), location.getY() + 0.25 + ((lines.size() - 1 - count) * 0.3), location.getZ());
                as.setInvulnerable(true);
                as.setInvisible(true);
                as.noCulling = true;
                as.noPhysics = true;
                as.setNoGravity(true);
                as.setCustomNameVisible(true);
                as.setMarker(true);
                as.setCustomName(new TextComponent(l));
                armorStands.add(as);
                EntityUtils.spawnEntity(as);
                count++;
            }
            return;
        }
        for (int i = 0; i < armorStands.size(); i++) {
            ArmorStand armorStand = armorStands.get(i);
            armorStand.level = ((CraftWorld) parent.getWorld()).getHandle();
            armorStand.moveTo(location.getX(), location.getY() + ((lines.size() - 1 - i) * 0.2), location.getZ());
            if (armorStand.getCustomName() == null || !armorStand.getCustomName().getString().equals(lines.get(i))) {
                armorStand.setCustomName(new TextComponent(lines.get(i)));
            }
        }
    }

}
