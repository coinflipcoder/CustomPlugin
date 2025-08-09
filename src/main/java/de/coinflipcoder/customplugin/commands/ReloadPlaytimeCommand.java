package de.coinflipcoder.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.coinflipcoder.customplugin.CustomPlugin;
import de.coinflipcoder.customplugin.managers.MessageManager;
import de.coinflipcoder.customplugin.managers.PlaytimeManager;

public class ReloadPlaytimeCommand  implements SimpleCommand {
    private final ProxyServer server;
    private final PlaytimeManager playtimeManager;

    public ReloadPlaytimeCommand(ProxyServer server, CustomPlugin plugin) {
        this.server = server;
        this.playtimeManager = plugin.getPlaytimeManager();
    }

    @Override
    public void execute(final Invocation invocation) {
        // Get Player
        Player player = (Player) invocation.source();

        // Check if the player has permission to use the command
        if (!player.hasPermission("custom.reloadplaytime") || !invocation.source().hasPermission("custom.*")) {
            player.sendMessage(MessageManager.INVALID_PERMISSION);
            return;
        };

        // reload the playtime file
        playtimeManager.loadPlaytimes();

        // Send the player a message that the playtime manager has been reloaded
        player.sendMessage(MessageManager.PLAYTIME_RELOAD);
    }
}
