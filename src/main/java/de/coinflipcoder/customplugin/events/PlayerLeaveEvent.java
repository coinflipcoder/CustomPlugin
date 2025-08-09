package de.coinflipcoder.customplugin.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.coinflipcoder.customplugin.CustomPlugin;
import de.coinflipcoder.customplugin.managers.MessageManager;
import de.coinflipcoder.customplugin.managers.PlaytimeManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.util.Objects;

public class PlayerLeaveEvent {
    private static final LuckPerms luckPermsAPI = LuckPermsProvider.get();
    private final ProxyServer server;
    private final PlaytimeManager playtimeManager;

    public PlayerLeaveEvent(ProxyServer server, CustomPlugin plugin) {
        this.server = server;
        this.playtimeManager = plugin.getPlaytimeManager();
    }

    @Subscribe
    public void onPlayerLeave(DisconnectEvent event) {
        // Get Player
        Player player = event.getPlayer();

        // Check if the disconnect event was successful, this will prevent players who weren't fully on the server to trigger the event
        if (!event.getLoginStatus().equals(DisconnectEvent.LoginStatus.SUCCESSFUL_LOGIN)) return;

        // Check if the player even was on a server
        if (player.getCurrentServer().isEmpty()) return;

        // Get the players prefix
        User user = luckPermsAPI.getUserManager().getUser(player.getUniqueId());
        String prefix = Objects.requireNonNull(user).getCachedData().getMetaData().getPrefix();

        // Remove the player from the playtime manager
        playtimeManager.endSession(player.getUniqueId());

        server.sendMessage(MessageManager.leave(prefix));
    }
}
