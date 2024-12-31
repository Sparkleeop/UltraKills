package me.pixelmaniastudios.customdeathmsg.config;

import org.bukkit.configuration.file.FileConfiguration;

import me.pixelmaniastudios.customdeathmsg.CustomDeathMessages;

public class ConfigManager {

    private CustomDeathMessages plugin;
    private FileConfiguration config;

    public ConfigManager(CustomDeathMessages plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
    }
}
