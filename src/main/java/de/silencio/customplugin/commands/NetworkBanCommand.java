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
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NetworkBanCommand implements SimpleCommand {
    private final ProxyServer proxyServer;
    private static final MiniMessage mm = MiniMessage.miniMessage();

    final static Component invalidPermission = mm.deserialize("<red>You don't have permission to use this command.");
    final static Component banMessage = mm.deserialize("<red>You have been banned from the network.");

    public NetworkBanCommand(ProxyServer proxyServer) { this.proxyServer = proxyServer; }

    @Override
    public void execute(final Invocation invocation) {
        if (!invocation.source().hasPermission("custom.networkban") || !invocation.source().hasPermission("custom.*")) {
            invocation.source().sendMessage(invalidPermission);
            return;
        }
        if (invocation.arguments().length == 1) {
            proxyServer.getPlayer(invocation.arguments()[0]).ifPresent(target -> target.disconnect(banMessage));
            for (RegisteredServer s : proxyServer.getAllServers()) {
                // TODO: ban on server
            }
            invocation.source().sendMessage(Component.text(invocation.arguments()[0] + " has been banned from the network."));
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        List<String> returnList = new ArrayList<>();

        if (!invocation.source().hasPermission("custom.networkban") || !invocation.source().hasPermission("custom.*")) { return CompletableFuture.completedFuture(List.of()); }
        for (Player p : proxyServer.getAllPlayers()) { returnList.add(p.getUsername()); }
        return CompletableFuture.completedFuture(returnList);
    }
}
