package me.pixelmaniastudios.customdeathmsg.listeners;

import me.pixelmaniastudios.customdeathmsg.CustomDeathMessages;
import me.pixelmaniastudios.customdeathmsg.utils.MessageUtils;
import me.pixelmaniastudios.customdeathmsg.webhook.WebhookUtils;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private final CustomDeathMessages plugin;

    public DeathListener(CustomDeathMessages plugin) {
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
        String cause = event.getEntity().getLastDamageCause() != null
                ? event.getEntity().getLastDamageCause().getCause().name().toLowerCase()
                : "default";

        String messageTemplate = config.getString("deathMessages." + cause, config.getString("deathMessages.default"));
        String player = event.getEntity().getName();
        String killer = event.getEntity().getKiller() != null ? event.getEntity().getKiller().getName() : "unknown";

        return messageTemplate.replace("{player}", player).replace("{killer}", killer);
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
