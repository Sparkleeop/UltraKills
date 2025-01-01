package me.pixelmaniastudios.ultrakills;

import org.bukkit.plugin.java.JavaPlugin;

import me.pixelmaniastudios.ultrakills.commands.ReloadCommand;
import me.pixelmaniastudios.ultrakills.config.ConfigManager;
import me.pixelmaniastudios.ultrakills.listeners.DeathListener;

public class UltraKills extends JavaPlugin {

    private static UltraKills instance;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;

        // Initialize configuration manager
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        // Register events and commands
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        getCommand("cdmreload").setExecutor(new ReloadCommand(this)); // Register reload command

        getLogger().info("UltraKills plugin enabled!");
    }


    @Override
    public void onDisable() {
        getLogger().info("UltraKills plugin disabled!");
    }

    public static UltraKills getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}