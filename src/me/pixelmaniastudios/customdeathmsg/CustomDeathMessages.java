package me.pixelmaniastudios.customdeathmsg;

import org.bukkit.plugin.java.JavaPlugin;

import me.pixelmaniastudios.customdeathmsg.config.ConfigManager;
import me.pixelmaniastudios.customdeathmsg.listeners.DeathListener;

public class CustomDeathMessages extends JavaPlugin {

    private static CustomDeathMessages instance;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;

        // Initialize and load the config manager
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        // Register the DeathListener and pass the plugin instance
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);

        getLogger().info("CustomDeathMessages plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("CustomDeathMessages plugin disabled!");
    }

    public static CustomDeathMessages getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
