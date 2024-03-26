package de.silencio.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SendCommand implements SimpleCommand {

    private final ProxyServer server;

    public SendCommand(ProxyServer server) { this.server = server; }
    @Override
    public void execute(final Invocation invocation) {
        if (invocation.source().hasPermission("custom.send")) {
            if (invocation.arguments().length == 2) {
                server.getPlayer(invocation.arguments()[0]).get().createConnectionRequest(server.getServer(invocation.arguments()[1]).get()).connect();
            }
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        String[] currentArgs = invocation.arguments();
        List<String> returnList = new ArrayList<>();

        if (invocation.source().hasPermission("custom.send")) {
            if (currentArgs.length == 1) {
                for (Player p : server.getAllPlayers()) { returnList.add(p.getUsername()); }
                return CompletableFuture.completedFuture(returnList);
            } else if (currentArgs.length == 2) {
                for (RegisteredServer p : server.getAllServers()) { returnList.add(p.getServerInfo().getName()); }
                return CompletableFuture.completedFuture(returnList);
            }
        }
        return CompletableFuture.completedFuture(List.of());
    }
}
