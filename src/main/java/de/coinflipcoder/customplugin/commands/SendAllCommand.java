package de.coinflipcoder.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.coinflipcoder.customplugin.managers.MessageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SendAllCommand implements SimpleCommand {
    private final ProxyServer server;

    public SendAllCommand(ProxyServer server) { this.server = server; }

    @Override
    public void execute(final Invocation invocation) {
        if (!invocation.source().hasPermission("custom.sendall") || !invocation.source().hasPermission("custom.*")) {
            invocation.source().sendMessage(MessageManager.INVALID_PERMISSION);
            return;
        }
        if (invocation.arguments().length == 1) {
            for (Player p : server.getAllPlayers()) { p.createConnectionRequest(server.getServer(invocation.arguments()[0]).get()).connect(); }
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        List<String> returnList = new ArrayList<>();

        if (!invocation.source().hasPermission("custom.sendall") || !invocation.source().hasPermission("custom.*")) { return CompletableFuture.completedFuture(List.of()); }
        for (RegisteredServer p : server.getAllServers()) { returnList.add(p.getServerInfo().getName()); }
        return CompletableFuture.completedFuture(returnList);
    }
}
