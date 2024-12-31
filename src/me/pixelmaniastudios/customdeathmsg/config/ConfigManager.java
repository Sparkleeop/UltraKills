package me.pixelmaniastudios.customdeathmsg.config;

import me.pixelmaniastudios.customdeathmsg.CustomDeathMessages;
import org.bukkit.ChatColor;

public class ConfigManager {

    private final CustomDeathMessages plugin;

    public ConfigManager(CustomDeathMessages plugin) {
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
