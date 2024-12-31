package me.pixelmaniastudios.customdeathmsg;

import org.bukkit.plugin.java.JavaPlugin;

import me.pixelmaniastudios.customdeathmsg.commands.ReloadCommand;
import me.pixelmaniastudios.customdeathmsg.config.ConfigManager;
import me.pixelmaniastudios.customdeathmsg.listeners.DeathListener;

public class CustomDeathMessages extends JavaPlugin {

    private static CustomDeathMessages instance;
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
