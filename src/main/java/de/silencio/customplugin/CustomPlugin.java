package de.silencio.customplugin;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.silencio.customplugin.commands.*;
import de.silencio.customplugin.events.PlayerJoinEvent;
import org.slf4j.Logger;

@Plugin(
        id = "customplugin",
        name = "CustomPlugin",
        version = BuildConstants.VERSION,
        description = "A custom plugin for my network needs.",
        url = "devlencio.net",
        authors = {"Silencio"},
        dependencies = {
                @Dependency(id = "luckperms")
        }
)
public class CustomPlugin {

    private final Logger logger;
    private final ProxyServer server;

    @Inject
    public CustomPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
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
        CommandMeta hardBanCommand = commandManager.metaBuilder("hardban")
                .plugin(this)
                .build();
        CommandMeta sayCommand = commandManager.metaBuilder("sayall")
                .plugin(this)
                .build();
        CommandMeta helpCommand = commandManager.metaBuilder("help")
                .aliases("customhelp")
                .plugin(this)
                .build();

        // Listeners
        eventManager.register(this, new PlayerJoinEvent());

        // Commands
        commandManager.register(nickCommandMeta, new NickCommand(server));
        commandManager.register(infoCommandMeta, new InfoCommand());
        commandManager.register(afkCommandMeta, new AfkCommand(server));
        commandManager.register(lobbyCommandMeta, new LobbyCommand(server));
        commandManager.register(sendCommandMeta, new SendCommand(server));
        commandManager.register(sendAllCommandMeta, new SendAllCommand(server));
        commandManager.register(hardBanCommand, new HardBanCommand(server));
        commandManager.register(sayCommand, new SayCommand(server));
        commandManager.register(helpCommand, new HelpCommand(server, this));

        logger.info("Custom Plugin started up.");
    }
}
