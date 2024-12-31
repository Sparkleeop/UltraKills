package me.pixelmaniastudios.customdeathmsg.commands;

import me.pixelmaniastudios.customdeathmsg.CustomDeathMessages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {

    private final CustomDeathMessages plugin;

    public ReloadCommand(CustomDeathMessages plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("customdeathmessages.admin")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                return true;
            }
        }

        // Reload the configuration
        plugin.getConfigManager().loadConfig();
        sender.sendMessage(ChatColor.GREEN + "CustomDeathMessages configuration reloaded.");
        return true;
    }
}
