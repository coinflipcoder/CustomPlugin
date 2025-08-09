package de.coinflipcoder.customplugin.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.coinflipcoder.customplugin.CustomPlugin;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlaytimeManager {

    private final Logger logger;
    private final File playtimeFile;
    private Map<UUID, Long> totalPlaytimes = new HashMap<>();
    private final Map<UUID, Instant> activeSessions = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public PlaytimeManager(CustomPlugin plugin, Path dataDirectory) {
        this.logger = plugin.getLogger();
        this.playtimeFile = new File(dataDirectory.toFile(), "playtime.json");
        checkAndCreateFile();
        loadPlaytimes();
    }

    public void startSession(UUID uuid) {
        activeSessions.put(uuid, Instant.now());
    }

    public void endSession(UUID uuid) {
        Instant joinTime = activeSessions.remove(uuid);

        long secondsPlayed = Duration.between(joinTime, Instant.now()).getSeconds();
        totalPlaytimes.merge(uuid, secondsPlayed, Long::sum);
        savePlaytimes();
    }

    public Instant getSessionStart(UUID uuid) {
        return activeSessions.get(uuid);
    }

    public long getTotalPlaytime(UUID uuid) {
        return totalPlaytimes.getOrDefault(uuid, 0L);
    }

    private void checkAndCreateFile() {
        if (!playtimeFile.exists()) {
            try {
                playtimeFile.createNewFile();
            } catch (IOException e) {
                logger.error("Could not create playtime.json", e);
            }
        }
    }

    private void savePlaytimes() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(playtimeFile, totalPlaytimes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPlaytimes() {
        try {
            totalPlaytimes = objectMapper.readValue(playtimeFile, new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Component calculatePlaytime(UUID uuid) {
        Instant joinTime = getSessionStart(uuid);
        long totalPlaytime = getTotalPlaytime(uuid);

        if (joinTime != null) {
            long currentPlaytime = Duration.between(joinTime, Instant.now()).getSeconds();

            long totalHours = (totalPlaytime + currentPlaytime) / 3600;
            long totalMinutes = ((totalPlaytime + currentPlaytime) % 3600) / 60;

            long currentHours = currentPlaytime / 3600;
            long currentMinutes = (currentPlaytime % 3600) / 60;

            String total = String.format("%02d hours, %02d minutes", totalHours, totalMinutes);
            String current = String.format("%02d hours, %02d minutes", currentHours, currentMinutes);

            return MessageManager.playTime(total, current);
        } else {
            long totalHours = totalPlaytime / 3600;
            long totalMinutes = (totalPlaytime % 3600) / 60;

            String total = String.format("%02d hours, %02d minutes", totalHours, totalMinutes);

            return MessageManager.playTime(total, "00 hours, 00 minutes");
        }
    }
}
