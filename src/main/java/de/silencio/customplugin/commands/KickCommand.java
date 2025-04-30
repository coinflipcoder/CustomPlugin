package de.silencio.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.silencio.customplugin.managers.MessageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class KickCommand implements SimpleCommand {
    private final ProxyServer server;

    public KickCommand(ProxyServer server) { this.server = server; }

    @Override
    public void execute(final Invocation invocation) {
        Player player = (Player) invocation.source();

        if (!player.hasPermission("custom.kick") || !invocation.source().hasPermission("custom.*")) {
            player.sendMessage(MessageManager.INVALID_PERMISSION);
            return;
        };

        if (invocation.arguments().length != 1) {
            player.sendMessage(MessageManager.INVALID_USAGE);
            return;
        };

        Player target = server.getPlayer(invocation.arguments()[0]).orElse(null);
        if (target == null) {
            player.sendMessage(MessageManager.INVALID_PLAYER);
            return;
        }

        target.disconnect(MessageManager.KICK_MESSAGE);
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        List<String> returnList = new ArrayList<>();

        if (!invocation.source().hasPermission("custom.kick") || !invocation.source().hasPermission("custom.*")) { return CompletableFuture.completedFuture(List.of()); }
        for (Player p : server.getAllPlayers()) { returnList.add(p.getUsername()); }
        return CompletableFuture.completedFuture(returnList);
    }
}
