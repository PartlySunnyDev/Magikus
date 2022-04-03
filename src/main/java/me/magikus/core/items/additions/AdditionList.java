package me.magikus.core.items.additions;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import me.magikus.core.items.MagikusItem;
import me.magikus.core.items.ModifierType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdditionList {

    private final Map<AdditionInfo, Addition> additionList = new HashMap<>();
    private ModifierType accepting = ModifierType.ANY;
    private MagikusItem parent;

    public AdditionList() {
    }

    public AdditionList(ModifierType accepting) {
        this.accepting = accepting;
    }

    public AdditionList(ModifierType accepting, MagikusItem parent) {
        this.accepting = accepting;
        this.parent = parent;
    }

    public void addAdditions(AdditionInfo addition, int count) {
        if (accepting != ModifierType.ANY && addition.type() != accepting) {
            throw new IllegalArgumentException("This list does not accept type " + addition.type());
        }
        if (parent != null && addition.cannotApply(parent)) {
            return;
        }
        Addition target = additionList.get(addition);
        for (AdditionInfo a : additionList.keySet()) {
            if (a.id().equals(addition.id())) {
                target = additionList.get(a);
            }
        }
        if (target == null) {
            try {
                Addition value;
                value = addition.cl().getDeclaredConstructor().newInstance();
                value.setAmount(count);
                additionList.put(addition, value);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                e.printStackTrace();
            }
            if (parent != null) {
                parent.updateMagikusItem();
            }
            return;
        }
        target.setAmount(target.amount() + count);
        if (target.amount() > target.type().maxAdditions()) {
            target.setAmount(target.type().maxAdditions());
        }
        if (checkZero(target)) {
            additionList.remove(addition);
        }
        if (parent != null) {
            parent.updateMagikusItem();
        }
    }

    public void addAdditions(NBTCompound additions) {
        if (additions == null) {
            return;
        }
        for (String s : additions.getKeys()) {
            addAdditions(AdditionManager.getAddition(s), additions.getInteger(s));
        }
    }

    public void setList(AdditionList newList) {
        if (newList.accepting() != accepting()) {
            throw new IllegalArgumentException("Lists do not match types");
        }
        additionList.clear();
        additionList.putAll(newList.additionList);
    }

    public void addAddition(AdditionInfo type) {
        addAdditions(type, 1);
    }

    public void addAddition(Addition addition) {
        if (accepting != ModifierType.ANY && addition.type().type() != accepting) {
            throw new IllegalArgumentException("This list does not accept type " + addition.type());
        }
        if (parent != null && addition.type().cannotApply(parent)) {
            return;
        }
        Addition target = additionList.get(addition.type);
        if (target == null) {
            additionList.put(addition.type, addition);
            if (parent != null) {
                parent.updateMagikusItem();
            }
            return;
        }
        target.setAmount(target.amount() + addition.amount());
        if (target.amount() > target.type().maxAdditions()) {
            target.setAmount(target.type().maxAdditions());
        }
        if (checkZero(target)) {
            additionList.remove(addition.type);
        }
        if (parent != null) {
            parent.updateMagikusItem();
        }
    }

    public void removeAdditions(AdditionInfo addition, int count) {
        if (accepting != ModifierType.ANY && addition.type() != accepting) {
            throw new IllegalArgumentException("This list does not contain type " + addition.type());
        }
        Addition target = additionList.get(addition);
        if (target == null) {
            return;
        }
        target.setAmount(target.amount() - count);
        if (checkZero(target)) {
            additionList.remove(addition);
        }
        if (parent != null) {
            parent.updateMagikusItem();
        }
    }

    public void removeAddition(AdditionInfo type) {
        removeAdditions(type, 1);
    }

    public void completelyRemove(AdditionInfo addition) {
        if (accepting != ModifierType.ANY && addition.type() != accepting) {
            throw new IllegalArgumentException("This list does not contain type " + addition.type());
        }
        additionList.remove(addition);
        if (parent != null) {
            parent.updateMagikusItem();
        }
    }

    public NBTItem applyAdditions(NBTItem item) {
        NBTCompound c;
        if (accepting == ModifierType.ANY) {
            c = item.addCompound("additions");
        } else if (accepting == ModifierType.STAT) {
            c = item.addCompound("statAdditions");
        } else if (accepting == ModifierType.ABILITY) {
            c = item.addCompound("abilityAdditions");
        } else if (accepting == ModifierType.RARITY) {
            c = item.addCompound("rarityAdditions");
        } else {
            c = item.addCompound("additions");
        }
        for (Addition a : additionList.values()) {
            if (!item.getBoolean("mg_unique")) {
                item.setBoolean("mg_unique", true);
            }
            c.setInteger(a.type().id(), (c.getInteger(a.type.id()) == null ? a.amount() : c.getInteger(a.type.id())) + a.amount());
        }
        return item;
    }

    public Addition[] asList() {
        Addition[] returned = new Addition[additionList.size()];
        int count = 0;
        for (Addition s : additionList.values()) {
            returned[count] = s;
            count++;
        }
        return returned;
    }

    public Map<AdditionInfo, Addition> additionList() {
        return additionList;
    }

    public ArrayList<Addition> asArrayList() {
        return new ArrayList<>(additionList.values());
    }

    public IStatAddition[] asStatList() {
        if (accepting != ModifierType.STAT) {
            throw new IllegalArgumentException("This list cannot be converted to a StatList");
        }
        IStatAddition[] returned = new IStatAddition[additionList.size()];
        int count = 0;
        for (Addition s : additionList.values()) {
            returned[count] = (IStatAddition) s;
            count++;
        }
        return returned;
    }

    public IAbilityAddition[] asAbilityList() {
        if (accepting != ModifierType.ABILITY) {
            throw new IllegalArgumentException("This list cannot be converted to a StatList");
        }
        IAbilityAddition[] returned = new IAbilityAddition[additionList.size()];
        int count = 0;
        for (Addition s : additionList.values()) {
            returned[count] = (IAbilityAddition) s;
            count++;
        }
        return returned;
    }

    public IRarityAddition[] asRarityList() {
        if (accepting != ModifierType.RARITY) {
            throw new IllegalArgumentException("This list cannot be converted to a StatList");
        }
        IRarityAddition[] returned = new IRarityAddition[additionList.size()];
        int count = 0;
        for (Addition s : additionList.values()) {
            returned[count] = (IRarityAddition) s;
            count++;
        }
        return returned;
    }


    public ModifierType accepting() {
        return accepting;
    }

    public boolean checkZero(Addition a) {
        return a.amount < 1;
    }
}
