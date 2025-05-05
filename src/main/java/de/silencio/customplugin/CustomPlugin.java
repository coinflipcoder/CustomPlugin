package de.silencio.customplugin;

//import com.github.retrooper.packetevents.PacketEvents;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import de.silencio.customplugin.commands.*;
import de.silencio.customplugin.events.*;
import de.silencio.customplugin.managers.BanManager;
import de.silencio.customplugin.managers.PlaytimeManager;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "customplugin",
        name = "CustomPlugin",
        version = "1.6",
        description = "A custom plugin for my network needs.",
        url = "devlencio.net",
        authors = {"Silencio"},
        dependencies = {
                @Dependency(id = "luckperms"),
                //@Dependency(id = "packetevents")
        }
)
public class CustomPlugin {
    private final Logger logger;
    private final ProxyServer server;
    private final BanManager banManager;
    private final PlaytimeManager playtimeManager;


    @Inject
    public CustomPlugin(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.banManager = new BanManager(this, dataDirectory);
        this.playtimeManager = new PlaytimeManager(this, dataDirectory);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager commandManager = server.getCommandManager();
        EventManager eventManager = server.getEventManager();

        // CommandMeta
        CommandMeta nickCommandMeta = commandManager.metaBuilder("nick")
                .aliases("nickname")
                .plugin(this)
                .build();
        CommandMeta infoCommandMeta = commandManager.metaBuilder("info")
                .plugin(this)
                .build();
        CommandMeta mapCommandMeta = commandManager.metaBuilder("map")
                .plugin(this)
                .build();
        CommandMeta afkCommandMeta = commandManager.metaBuilder("afk")
                .plugin(this)
                .build();
        CommandMeta lobbyCommandMeta = commandManager.metaBuilder("lobby")
                .aliases("hub")
                .plugin(this)
                .build();
        CommandMeta sendCommandMeta = commandManager.metaBuilder("sendplayer")
                .plugin(this)
                .build();
        CommandMeta sendAllCommandMeta = commandManager.metaBuilder("sendeveryone")
                .plugin(this)
                .build();
        CommandMeta networkBanCommandMeta = commandManager.metaBuilder("networkban")
                .plugin(this)
                .build();
        CommandMeta networkUnbanCommandMeta = commandManager.metaBuilder("networkunban")
                .plugin(this)
                .build();
        CommandMeta sayCommandMeta = commandManager.metaBuilder("sayall")
                .plugin(this)
                .build();
        CommandMeta helpCommandMeta = commandManager.metaBuilder("help")
                .aliases("customhelp")
                .plugin(this)
                .build();
        CommandMeta kickCommandMeta = commandManager.metaBuilder("kick")
                .plugin(this)
                .build();
        CommandMeta playtimeCommandMeta = commandManager.metaBuilder("playtime")
                .plugin(this)
                .build();
        CommandMeta reloadPlaytimeCommandMeta = commandManager.metaBuilder("reloadplaytime")
                .plugin(this)
                .build();
        CommandMeta discordCommandMeta = commandManager.metaBuilder("discord")
                .plugin(this)
                .build();

        // Listeners
        eventManager.register(this, new PlayerJoinEvent(server, this));
        eventManager.register(this, new PlayerLeaveEvent(server, this));
        eventManager.register(this, new PlayerServerChangeEvent(server));
        eventManager.register(this, new PlayerMessageEvent(server));

        // Commands
        commandManager.register(nickCommandMeta, new NickCommand(server));
        commandManager.register(mapCommandMeta, new MapCommand());
        commandManager.register(discordCommandMeta, new DiscordCommand());
        commandManager.register(infoCommandMeta, new InfoCommand());
        commandManager.register(afkCommandMeta, new AfkCommand(server));
        commandManager.register(lobbyCommandMeta, new LobbyCommand(server));
        commandManager.register(sendCommandMeta, new SendCommand(server));
        commandManager.register(sendAllCommandMeta, new SendAllCommand(server));
        commandManager.register(networkBanCommandMeta, new NetworkBanCommand(server, this));
        commandManager.register(networkUnbanCommandMeta, new NetworkUnbanCommand(server, this));
        commandManager.register(sayCommandMeta, new SayCommand(server));
        commandManager.register(helpCommandMeta, new HelpCommand(server, this));
        commandManager.register(kickCommandMeta, new KickCommand(server));
        commandManager.register(playtimeCommandMeta, new PlaytimeCommand(server, this));
        commandManager.register(reloadPlaytimeCommandMeta, new ReloadPlaytimeCommand(server, this));

        //PacketEvents.getAPI().getEventManager().registerListener(new SafeServerPacket());

        logger.info("Custom Plugin started up.");
    }

    public BanManager getBanManager() {
        return banManager;
    }

    public PlaytimeManager getPlaytimeManager() {
        return playtimeManager;
    }

    public Logger getLogger() {
        return logger;
    }
}
