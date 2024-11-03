package de.silencio.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.PluginMessageEncoder;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.silencio.customplugin.CustomPlugin;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NetworkBanCommand implements SimpleCommand {
    private final ProxyServer proxyServer;

    public NetworkBanCommand(ProxyServer proxyServer) { this.proxyServer = proxyServer; }

    @Override
    public void execute(final Invocation invocation) {
        if (invocation.source().hasPermission("custom.networkban") || invocation.source().hasPermission("custom.*")) {
            if (invocation.arguments().length == 1) {
                proxyServer.getPlayer(invocation.arguments()[0]).ifPresent(target -> target.disconnect(Component.text("You have been banned from the network.")));
                for (RegisteredServer s : proxyServer.getAllServers()) {
                    // ban on server
                }
                invocation.source().sendMessage(Component.text(invocation.arguments()[0] + " has been banned from the network."));
            }
        } else {
            invocation.source().sendMessage(Component.text("You don't have permission to execute this command."));
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        List<String> returnList = new ArrayList<>();

        if (invocation.source().hasPermission("custom.networkban") || invocation.source().hasPermission("custom.*")) {
            for (Player p : proxyServer.getAllPlayers()) {
                returnList.add(p.getUsername());
            }
            return CompletableFuture.completedFuture(returnList);
        }
        return CompletableFuture.completedFuture(List.of());
    }
}
