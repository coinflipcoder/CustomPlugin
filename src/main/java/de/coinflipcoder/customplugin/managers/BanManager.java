package de.coinflipcoder.customplugin.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.coinflipcoder.customplugin.CustomPlugin;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BanManager {
    private final Logger logger;
    private final File bannedPlayersFile;
    private Set<UUID> bannedPlayers = new HashSet<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BanManager(CustomPlugin plugin, Path dataDirectory) {
        this.logger = plugin.getLogger();
        this.bannedPlayersFile = new File(dataDirectory.toFile(), "banned_players.json");
        checkAndCreateFile();
        loadBannedPlayers();
    }

    private void checkAndCreateFile() {
        if (!bannedPlayersFile.exists()) {
            try {
                bannedPlayersFile.createNewFile();
            } catch (IOException e) {
                logger.error("Could not create banned_players.json", e);
            }
        }
    }

    private void loadBannedPlayers() {
        try {
            bannedPlayers = objectMapper.readValue(bannedPlayersFile, new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBannedPlayers() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(bannedPlayersFile, bannedPlayers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<UUID> getBannedPlayers() {
        return bannedPlayers;
    }

    public void banPlayer(UUID uuid) {
        if (isBanned(uuid)) return;
        bannedPlayers.add(uuid);
        saveBannedPlayers();
    }

    public void unbanPlayer(UUID uuid) {
        if (!isBanned(uuid)) return;
        bannedPlayers.remove(uuid);
        saveBannedPlayers();
    }

    public boolean isBanned(UUID uuid) {
        return bannedPlayers.contains(uuid);
    }

    public UUID getUUIDFromUsername(String username) {
        try {
            // Use Mojang's API to resolve username to UUID
            String urlString = "https://api.mojang.com/users/profiles/minecraft/" + username;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Read the response and parse it with Jackson
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String response;
                StringBuilder content = new StringBuilder();
                while ((response = reader.readLine()) != null) {
                    content.append(response);
                }
                reader.close();

                if (!content.isEmpty()) {
                    JsonNode jsonNode = objectMapper.readTree(String.valueOf(content));

                    String uuidStringWithoutDashes = jsonNode.get("id").asText();

                    // Convert the UUID string to a UUID object
                    return UUID.fromString(uuidStringWithoutDashes.replaceFirst(
                            "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public CompletableFuture<UUID> getUUIDFromUsernameAsync(String username) {
        return CompletableFuture.supplyAsync(() -> getUUIDFromUsername(username));
    }
}
