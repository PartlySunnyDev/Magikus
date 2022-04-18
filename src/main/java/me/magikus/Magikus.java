package me.magikus;

import me.magikus.additions.AdditionRegister;
import me.magikus.core.ConsoleLogger;
import me.magikus.core.commands.*;
import me.magikus.core.entities.EntityUpdater;
import me.magikus.core.entities.damage.DamageManager;
import me.magikus.core.generation.biomes.spawning.EntitySpawnManager;
import me.magikus.core.items.ItemUpdater;
import me.magikus.core.magic.spells.SpellCastListener;
import me.magikus.core.player.PlayerStatManager;
import me.magikus.core.player.PlayerUpdater;
import me.magikus.core.tools.classes.ConfigManager;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import static me.magikus.abilities.AbilityRegister.registerAbilities;
import static me.magikus.core.generation.biomes.BiomeRegister.registerBiomeEntities;
import static me.magikus.core.player.BaseStatManager.initializeBaseStats;
import static me.magikus.core.player.BaseStatManager.repairDefaultStats;
import static me.magikus.data.recipes.RecipeRegister.registerRecipes;
import static me.magikus.entities.EntityRegister.registerEntityAbilities;
import static me.magikus.entities.EntityRegister.registerEntityInfos;
import static me.magikus.guis.GuiRegister.registerGuis;
import static me.magikus.items.ItemRegister.registerItems;
import static me.magikus.magic.MagicRegister.registerSpells;

public final class Magikus extends JavaPlugin {

    public static ConfigManager configManager;

    @Override
    public void onLoad() {
        try {
            registerEntityInfos();
            registerEntityAbilities();
            registerBiomeEntities();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateEverything() {
        for (Player p : getServer().getOnlinePlayers()) {
            Inventory inventory = p.getInventory();
            ItemUpdater.updateVanilla(inventory, p);
            ItemUpdater.idify(inventory, p);
            ItemUpdater.updateInventory(inventory, p);
        }

        for (World w : getServer().getWorlds()) {
            for (Entity e : w.getEntities()) {
                if (!e.getType().isAlive() || e.getType() == EntityType.ARMOR_STAND || e.getType() == EntityType.PLAYER) {
                    continue;
                }
                EntityUpdater.updateStats(e);
                EntityUpdater.updateName(e);
            }
        }
    }

    @SuppressWarnings("all")
    private void registerCommands() {
        getCommand("mgive").setExecutor(new MagikusGive());
        getCommand("msummon").setExecutor(new MagikusSummon());
        getCommand("madd").setExecutor(new MagikusAddAddition());
        getCommand("mascend").setExecutor(new MagikusAscend());
        getCommand("menhance").setExecutor(new MagikusEnhance());
        getCommand("menchant").setExecutor(new MagikusEnchant());
        getCommand("mgui").setExecutor(new MagikusOpenGui());
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new ItemUpdater(), this);
        this.getServer().getPluginManager().registerEvents(new EntityUpdater(getServer()), this);
        this.getServer().getPluginManager().registerEvents(new DamageManager(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerStatManager(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerUpdater(getServer()), this);
        this.getServer().getPluginManager().registerEvents(new SpellCastListener(), this);
        this.getServer().getPluginManager().registerEvents(new EntitySpawnManager(), this);
    }

    @Override
    public void onDisable() {
        ConsoleLogger.console("Shutting down Magikus plugin...");
    }

    @Override
    public void onEnable() {
        configManager = new ConfigManager(JavaPlugin.getPlugin(Magikus.class));
        SpellCastListener.initPlayers(getServer());
        repairDefaultStats();
        initializeBaseStats(this);
        registerCommands();
        AdditionRegister.registerEnchants();
        registerListeners();
        registerItems();
        registerAbilities();
        registerSpells();
        AdditionRegister.registerAdditions();
        AdditionRegister.registerAscensions();
        registerRecipes();
        registerGuis();
        updateEverything();
        ConsoleLogger.console("Loaded Magikus plugin on version " + getDescription().getVersion() + "...");
    }

}
