package de.silencio.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.silencio.customplugin.CustomPlugin;
import de.silencio.customplugin.managers.BanManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NetworkBanCommand implements SimpleCommand {
    private final ProxyServer server;
    private final CustomPlugin plugin;
    private final BanManager banManager;
    private static final MiniMessage mm = MiniMessage.miniMessage();

    static final Component invalidPlayer = mm.deserialize("<red>Player not found.");
    static final Component invalidUsage = mm.deserialize("<red>Invalid command usage.");
    static final Component alreadyBanned = mm.deserialize("<red>Player is already banned.");
    final static Component banMessage = mm.deserialize("<red>You have been banned from the network.");
    final static Component invalidPermission = mm.deserialize("<red>You don't have permission to use this command.");

    public NetworkBanCommand(ProxyServer server, CustomPlugin plugin) {
        this.server = server;
        this.plugin = plugin;
        this.banManager = plugin.getBanManager();
    }

    @Override
    public void execute(final Invocation invocation) {
        // Check if the player has the permission to use this command
        if (!invocation.source().hasPermission("custom.networkban") || !invocation.source().hasPermission("custom.*")) {
            invocation.source().sendMessage(invalidPermission);
            return;
        }

        // Check if the command was used correctly
        if (invocation.arguments().length != 1) {
            invocation.source().sendMessage(invalidUsage);
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
                        invocation.source().sendMessage(invalidPlayer);
                        return;
                    }
                    if (banManager.isBanned(uuid)) {
                        invocation.source().sendMessage(alreadyBanned);
                        return;
                    }
                    // Ban the player
                    this.banManager.banPlayer(uuid);

                    // Send a message to the player who executed the command
                    final Component successMessage = mm.deserialize("<green>Successfully banned " + invocation.arguments()[0] + ".");
                    invocation.source().sendMessage(successMessage);
                }).schedule();
            });
            return;
        }

        // Player is online, ban the player
        target.disconnect(banMessage);
        this.banManager.banPlayer(target.getUniqueId());

        // Send a message to the player who executed the command
        final Component successMessage = mm.deserialize("<green>Successfully banned " + target.getUsername() + ".");
        invocation.source().sendMessage(successMessage);
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        List<String> returnList = new ArrayList<>();

        if (!invocation.source().hasPermission("custom.networkban") || !invocation.source().hasPermission("custom.*")) { return CompletableFuture.completedFuture(List.of()); }
        for (Player p : server.getAllPlayers()) { returnList.add(p.getUsername()); }
        return CompletableFuture.completedFuture(returnList);
    }
}
