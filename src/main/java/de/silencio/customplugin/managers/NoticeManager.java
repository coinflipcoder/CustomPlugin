package de.silencio.customplugin.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.silencio.customplugin.CustomPlugin;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class NoticeManager {
    private final Logger logger;
    private final File noticeFile;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public String message;

    public NoticeManager(CustomPlugin plugin, Path dataDirectory) {
        this.logger = plugin.getLogger();
        this.noticeFile = new File(dataDirectory.toFile(), "notice.json");
        checkAndCreateFile();
        loadNotice();
    }

    private void checkAndCreateFile() {
        if (!noticeFile.exists()) {
            try {
                noticeFile.createNewFile();
            } catch (IOException e) {
                logger.error("Could not create notice.json", e);
            }
        }
    }

    private void loadNotice() {
        try {
            message = objectMapper.readValue(noticeFile, new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNotice(String newMessage) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(noticeFile, newMessage);
            message = newMessage;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNotice() {
        return message;
    }

    public void clearNotice() {
        setNotice("");
    }

    public boolean hasNotice() {
        return message != null && !message.isEmpty();
    }
}
