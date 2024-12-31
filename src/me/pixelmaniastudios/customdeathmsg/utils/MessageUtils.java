package me.pixelmaniastudios.customdeathmsg.utils;

import org.bukkit.ChatColor;

public class MessageUtils {

    // Utility method to colorize text
    public static String colorize(String message) {
        if (message == null) return "";
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    // Utility method to strip color codes (useful for sending messages to Discord)
    public static String stripColor(String message) {
        if (message == null) return "";
        return ChatColor.stripColor(message);
    }
}
