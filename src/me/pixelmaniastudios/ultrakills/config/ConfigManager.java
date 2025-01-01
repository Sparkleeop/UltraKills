package me.pixelmaniastudios.ultrakills.config;

import me.pixelmaniastudios.ultrakills.UltraKills;
import org.bukkit.ChatColor;

public class ConfigManager {

    private final UltraKills plugin;

    public ConfigManager(UltraKills plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
    }

    public String formatDeathMessage(String message) {
        String prefix = plugin.getConfig().getString("prefix", "&7[&cDeath&7] ");
        return ChatColor.translateAlternateColorCodes('&', prefix + message);
    }
}