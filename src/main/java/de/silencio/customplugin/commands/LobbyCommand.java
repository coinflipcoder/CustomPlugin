package de.silencio.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

public final class LobbyCommand implements SimpleCommand {
    private final ProxyServer server;

    public LobbyCommand(ProxyServer server) {
        this.server = server;
    }
    @Override
    public void execute(final Invocation invocation) {
        Player player = (Player) invocation.source();
        player.createConnectionRequest(server.getServer("lobby").get()).connect();
    }
}
