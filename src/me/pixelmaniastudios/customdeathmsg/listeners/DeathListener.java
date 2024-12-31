package me.pixelmaniastudios.customdeathmsg.listeners;

import me.pixelmaniastudios.customdeathmsg.CustomDeathMessages;
import me.pixelmaniastudios.customdeathmsg.webhook.WebhookUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

        // Play the corresponding sound
        playDeathSound(player, deathCause);

        // Trigger the corresponding death effect
        triggerDeathEffect(player.getLocation(), deathCause);

        // Get the prefix and death message
        String prefix = colorize(config.getString("prefix", ""));
        String deathMessage = getDeathMessage(event);

        // Combine prefix and message
        String finalMessage = prefix + deathMessage;

        // Set the message
        event.setDeathMessage(colorize(finalMessage));

        // Send to Discord if webhook is enabled
        if (config.getBoolean("webhook.enabled")) {
            sendToDiscord(ChatColor.stripColor(finalMessage)); // Send plain text to Discord
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

    // Utility method to colorize text
    private String colorize(String message) {
        return message.replace("&", "§");
    }
    
    private String getDeathCause(PlayerDeathEvent event) {
        EntityDamageEvent.DamageCause cause = event.getEntity().getLastDamageCause().getCause();
        return cause.toString();
    }
    
    private void playDeathSound(Player player, String deathCause) {
        String soundKey = plugin.getConfig().getString("deathSounds." + deathCause, "OTHER");
        Sound sound = Sound.valueOf(soundKey);
        
        // Play the death sound
        player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
    }

    private void triggerDeathEffect(Location location, String deathCause) {
        String effectKey = plugin.getConfig().getString("deathEffects." + deathCause, "OTHER");
        Particle particle = Particle.valueOf(effectKey);
        
        // Trigger particle effect at the player's death location
        location.getWorld().spawnParticle(particle, location, 50);

    }
}


