package de.silencio.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NetworkUnbanCommand implements SimpleCommand {
    private final ProxyServer server;
    private static final MiniMessage mm = MiniMessage.miniMessage();

    final static Component invalidPermission = mm.deserialize("<red>You don't have permission to use this command.");

    public NetworkUnbanCommand(ProxyServer server) { this.server = server; }

    @Override
    public void execute(final Invocation invocation) {
        if (!invocation.source().hasPermission("custom.networkunban") || !invocation.source().hasPermission("custom.*")) {
            invocation.source().sendMessage(invalidPermission);
            return;
        }
        if (invocation.arguments().length == 1) {
            for (RegisteredServer s : server.getAllServers()) {
                // TODO: unban on server
            }
            invocation.source().sendMessage(Component.text(invocation.arguments()[0] + " has been unbanned from the network."));
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        List<String> returnList = new ArrayList<>();

        if (!invocation.source().hasPermission("custom.networkunban") || !invocation.source().hasPermission("custom.*")) { return CompletableFuture.completedFuture(List.of()); }
        // TODO: suggest banned players
        return CompletableFuture.completedFuture(returnList);
    }
}
