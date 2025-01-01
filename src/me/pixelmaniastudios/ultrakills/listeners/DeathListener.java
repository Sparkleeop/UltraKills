package me.pixelmaniastudios.ultrakills.listeners;

import me.pixelmaniastudios.ultrakills.UltraKills;
import me.pixelmaniastudios.ultrakills.utils.MessageUtils;
import me.pixelmaniastudios.ultrakills.webhook.WebhookUtils;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private final UltraKills plugin;

    public DeathListener(UltraKills plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String worldName = event.getEntity().getWorld().getName();
        FileConfiguration config = plugin.getConfig();

        // Check if the world is disabled for death messages
        if (config.getStringList("worldSettings.disabledWorlds").contains(worldName)) {
            return;
        }

        Player player = event.getEntity();
        String deathCause = getDeathCause(event);

        // Get the prefix and death message
        String prefix = MessageUtils.colorize(config.getString("prefix", ""));
        String deathMessage = getDeathMessage(event);

        // Combine prefix and message
        String finalMessage = prefix + deathMessage;

        // Set the message
        event.setDeathMessage(MessageUtils.colorize(finalMessage));

        // Send to Discord if webhook is enabled
        if (config.getBoolean("webhook.enabled")) {
            sendToDiscord(MessageUtils.stripColor(finalMessage)); // Send plain text to Discord
        }
    }

    private String getDeathMessage(PlayerDeathEvent event) {
        FileConfiguration config = plugin.getConfig();
        Player player = event.getEntity();

        // Determine the permission group based on the player's permissions
        String permissionGroup = "default"; // Default group
        for (String group : config.getConfigurationSection("permissionGroups").getKeys(false)) {
            if (group.equals("default")) continue; // Skip default for now
            for (String permission : config.getStringList("permissionGroups." + group + ".permissions")) {
                if (player.hasPermission(permission)) {
                    permissionGroup = group;
                    break;
                }
            }
        }

        // Fetch death messages for the determined permission group
        String causeKey = "default"; // Default death cause
        String killerName = "unknown";

        if (player.getLastDamageCause() != null) {
            EntityDamageEvent damageEvent = player.getLastDamageCause();
            EntityDamageEvent.DamageCause cause = damageEvent.getCause();

            if (cause != null) {
                causeKey = cause.name().toLowerCase();
            }

            // Check if the death was caused by another entity
            if (damageEvent instanceof org.bukkit.event.entity.EntityDamageByEntityEvent) {
                org.bukkit.event.entity.EntityDamageByEntityEvent entityDamageByEntityEvent =
                        (org.bukkit.event.entity.EntityDamageByEntityEvent) damageEvent;
                org.bukkit.entity.Entity damager = entityDamageByEntityEvent.getDamager();

                if (damager instanceof Player) {
                    killerName = ((Player) damager).getName(); // Killed by another player
                    causeKey = "killedby-player";
                } else {
                    killerName = damager.getName(); // Killed by mob or other entity
                    causeKey = "mob";
                }
            }
        }

        // Get the message template for the permission group and cause
        String messageTemplate = config.getString(
            "permissionGroups." + permissionGroup + ".messages." + causeKey,
            config.getString("permissionGroups." + permissionGroup + ".messages.default")
        );

        // Replace placeholders
        return messageTemplate
                .replace("{player}", player.getName())
                .replace("{killer}", killerName);
    }


    private void sendToDiscord(String message) {
        String webhookUrl = plugin.getConfig().getString("webhook.url");
        if (webhookUrl != null && !webhookUrl.isEmpty()) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> WebhookUtils.sendWebhook(webhookUrl, message));
        }
    }

    private String getDeathCause(PlayerDeathEvent event) {
        EntityDamageEvent.DamageCause cause = event.getEntity().getLastDamageCause().getCause();
        return cause.toString();
    }
}