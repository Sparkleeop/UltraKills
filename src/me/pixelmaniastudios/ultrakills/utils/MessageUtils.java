package me.pixelmaniastudios.ultrakills.utils;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtils {

    // Pattern to match hex color codes
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    // Utility method to colorize text with hex and legacy codes
    public static String colorize(String message) {
        if (message == null) return "";

        // Replace hex color codes with Bukkit's ChatColor format
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String hexCode = matcher.group(1);
            String bukkitColorCode = translateHexToBukkit(hexCode);
            matcher.appendReplacement(buffer, bukkitColorCode);
        }
        matcher.appendTail(buffer);

        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }

    // Utility method to strip color codes
    public static String stripColor(String message) {
        if (message == null) return "";
        return ChatColor.stripColor(message);
    }

    // Translate hex code to Bukkit's ChatColor format
    private static String translateHexToBukkit(String hex) {
        StringBuilder builder = new StringBuilder("§x");
        for (char c : hex.toCharArray()) {
            builder.append('§').append(c);
        }
        return builder.toString();
    }
}