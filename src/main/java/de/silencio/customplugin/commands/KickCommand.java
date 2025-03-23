package de.silencio.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class KickCommand implements SimpleCommand {
    private final ProxyServer server;
    private static final LuckPerms luckPermsAPI = LuckPermsProvider.get();
    private static final MiniMessage mm = MiniMessage.miniMessage();

    static final Component invalidUsage = mm.deserialize("<red>Invalid command usage.");
    static final Component invalidPermission = mm.deserialize("<red>You don't have permission to use this command.");

    public KickCommand(ProxyServer server) { this.server = server; }

    @Override
    public void execute(final Invocation invocation) {
        Player player = (Player) invocation.source();

        if (!player.hasPermission("custom.kick") || !invocation.source().hasPermission("custom.*")) {
            player.sendMessage(invalidPermission);
            return;
        };

        if (invocation.arguments().length != 1) {
            player.sendMessage(invalidUsage);
            return;
        };

        Player target = server.getPlayer(invocation.arguments()[0]).orElse(null);
        if (target == null) player.sendMessage(mm.deserialize("<red>Player not found."));

        assert target != null;
        target.disconnect(mm.deserialize("<red>You have been kicked from the server."));
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        List<String> returnList = new ArrayList<>();

        if (!invocation.source().hasPermission("custom.kick") || !invocation.source().hasPermission("custom.*")) { return CompletableFuture.completedFuture(List.of()); }
        for (Player p : server.getAllPlayers()) { returnList.add(p.getUsername()); }
        return CompletableFuture.completedFuture(returnList);
    }
}
