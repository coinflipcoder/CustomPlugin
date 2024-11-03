package de.silencio.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NetworkUnbanCommand implements SimpleCommand {
    private final ProxyServer server;

    public NetworkUnbanCommand(ProxyServer server) { this.server = server; }

    @Override
    public void execute(final Invocation invocation) {
        if (invocation.source().hasPermission("custom.networkunban") || invocation.source().hasPermission("custom.*")) {
            if (invocation.arguments().length == 1) {
                for (RegisteredServer s : server.getAllServers()) {
                    // unban on server
                }
                invocation.source().sendMessage(Component.text(invocation.arguments()[0] + " has been unbanned from the network."));
            }
        } else {
            invocation.source().sendMessage(Component.text("You don't have permission to execute this command."));
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        List<String> returnList = new ArrayList<>();

        if (invocation.source().hasPermission("custom.networkunban") || invocation.source().hasPermission("custom.*")) {
            for (Player p : server.getAllPlayers()) {
                returnList.add(p.getUsername());
            }
            return CompletableFuture.completedFuture(returnList);
        }
        return CompletableFuture.completedFuture(List.of());
    }
}
