package de.silencio.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HardBanCommand implements SimpleCommand {
    private final ProxyServer server;

    public HardBanCommand(ProxyServer server) { this.server = server; }

    @Override
    public void execute(final Invocation invocation) {
        if (invocation.source().hasPermission("custom.hardban")) {
            if (invocation.arguments().length == 1) {
                Player target;
                for (Player p : server.getAllPlayers()) {
                    if (p.getUsername().equals(invocation.arguments()[0])) {
                        target = p;
                        return;
                    }
                }
                for (RegisteredServer s : server.getAllServers()) {
                    // ban player target on all servers.
                }
            }
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        List<String> returnList = new ArrayList<>();

        if (invocation.source().hasPermission("custom.hardban")) {
            for (Player p : server.getAllPlayers()) {
                returnList.add(p.getUsername());
            }
            return CompletableFuture.completedFuture(returnList);
        }
        return CompletableFuture.completedFuture(List.of());
    }
}
