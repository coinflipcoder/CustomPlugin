package de.coinflipcoder.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.coinflipcoder.customplugin.CustomPlugin;
import de.coinflipcoder.customplugin.managers.BanManager;
import de.coinflipcoder.customplugin.managers.MessageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NetworkBanCommand implements SimpleCommand {
    private final ProxyServer server;
    private final CustomPlugin plugin;
    private final BanManager banManager;

    public NetworkBanCommand(ProxyServer server, CustomPlugin plugin) {
        this.server = server;
        this.plugin = plugin;
        this.banManager = plugin.getBanManager();
    }

    @Override
    public void execute(final Invocation invocation) {
        // Check if the player has the permission to use this command
        if (!invocation.source().hasPermission("custom.networkban") || !invocation.source().hasPermission("custom.*")) {
            invocation.source().sendMessage(MessageManager.INVALID_PERMISSION);
            return;
        }

        // Check if the command was used correctly
        if (invocation.arguments().length != 1) {
            invocation.source().sendMessage(MessageManager.INVALID_USAGE);
            return;
        }

        // Check if the player exists
        Player target = server.getPlayer(invocation.arguments()[0]).orElse(null);

        if (target == null) {
            // either the player is not online or the player does not exist
            banManager.getUUIDFromUsernameAsync(invocation.arguments()[0]).thenAccept(uuid -> {
                server.getScheduler().buildTask(plugin, () -> {
                    if (uuid == null) {
                        // player really does not exist
                        invocation.source().sendMessage(MessageManager.INVALID_PLAYER);
                        return;
                    }
                    if (banManager.isBanned(uuid)) {
                    invocation.source().sendMessage(MessageManager.ALREADY_BANNED);
                        return;
                    }
                    // Ban the player
                    this.banManager.banPlayer(uuid);

                    // Send a message to the player who executed the command
                    invocation.source().sendMessage(MessageManager.banSuccess(invocation.arguments()[0]));
                }).schedule();
            });
            return;
        }

        // Player is online, ban the player
        target.disconnect(MessageManager.BAN_MESSAGE);
        this.banManager.banPlayer(target.getUniqueId());

        // Send a message to the player who executed the command
        invocation.source().sendMessage(MessageManager.banSuccess(target.getUsername()));
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        List<String> returnList = new ArrayList<>();

        if (!invocation.source().hasPermission("custom.networkban") || !invocation.source().hasPermission("custom.*")) { return CompletableFuture.completedFuture(List.of()); }
        for (Player p : server.getAllPlayers()) { returnList.add(p.getUsername()); }
        return CompletableFuture.completedFuture(returnList);
    }
}
