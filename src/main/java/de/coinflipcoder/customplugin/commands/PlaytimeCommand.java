package de.coinflipcoder.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.coinflipcoder.customplugin.CustomPlugin;
import de.coinflipcoder.customplugin.managers.BanManager;
import de.coinflipcoder.customplugin.managers.MessageManager;
import de.coinflipcoder.customplugin.managers.PlaytimeManager;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class PlaytimeCommand implements SimpleCommand {
    private final ProxyServer server;
    private final PlaytimeManager playtimeManager;
    private final CustomPlugin plugin;
    private final BanManager banManager;

    public PlaytimeCommand(ProxyServer server, CustomPlugin plugin) {
        this.server = server;
        this.plugin = plugin;
        this.playtimeManager = plugin.getPlaytimeManager();
        this.banManager = plugin.getBanManager();
    }

    @Override
    public void execute(final Invocation invocation) {
        // Player gets own playtime
        if (invocation.arguments().length == 0) {
            // Get Player
            Player player = (Player) invocation.source();

            // Get the playtime of the player
            final Component message = playtimeManager.calculatePlaytime(player.getUniqueId());

            player.sendMessage(message);

        // Player gets another players playtime
        } else if (invocation.arguments().length == 1) {
            Player player = (Player) invocation.source();

            // Check if the player has the permission to change other players nicknames
            if (!invocation.source().hasPermission("custom.playtimeother") || !invocation.source().hasPermission("custom.*")) {
                invocation.source().sendMessage(MessageManager.INVALID_PERMISSION);
                return;
            }

            Player targetPlayer = server.getPlayer(invocation.arguments()[0]).orElse(null);

            if (targetPlayer == null) {
                // either the player is not online or the player does not exist
                banManager.getUUIDFromUsernameAsync(invocation.arguments()[0]).thenAccept(uuid -> {
                    server.getScheduler().buildTask(plugin, () -> {
                        if (uuid == null) {
                            // player really does not exist
                            invocation.source().sendMessage(MessageManager.INVALID_PLAYER);
                            return;
                        }
                        // Get the playtime from the offline player
                        final Component message = playtimeManager.calculatePlaytime(uuid);
                        player.sendMessage(message);
                    }).schedule();
                });
                return;
            }

            // Get the playtime of the target
            final Component message = playtimeManager.calculatePlaytime(targetPlayer.getUniqueId());

            // Send the message to the player
            player.sendMessage(message);
        } else invocation.source().sendMessage(MessageManager.INVALID_USAGE);
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        List<String> returnList = new ArrayList<>();

        if (!invocation.source().hasPermission("custom.playtimeother") || !invocation.source().hasPermission("custom.*")) { return CompletableFuture.completedFuture(List.of()); }
        for (Player p : server.getAllPlayers()) { returnList.add(p.getUsername()); }
        return CompletableFuture.completedFuture(returnList);
    }
}
