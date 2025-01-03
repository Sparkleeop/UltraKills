package me.pixelmaniastudios.ultrakills.webhook;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import me.pixelmaniastudios.ultrakills.UltraKills;
import org.bukkit.ChatColor;

public class WebhookUtils {

    public static void sendWebhook(String webhookUrl, String message) {
        try {
            // Remove color codes from the message
            String plainMessage = ChatColor.stripColor(message);

            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            // Prepare JSON payload with plain message
            String jsonPayload = "{\"content\": \"" + plainMessage + "\"}";

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonPayload.getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_NO_CONTENT) {
                throw new RuntimeException("Failed to send message to Discord. HTTP response code: " + responseCode);
            }

        } catch (Exception e) {
            UltraKills.getInstance().getLogger().severe("Error sending Discord webhook: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
