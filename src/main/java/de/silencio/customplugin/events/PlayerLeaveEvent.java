package de.silencio.customplugin.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import de.silencio.customplugin.managers.MessageManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.util.Objects;

public class PlayerLeaveEvent {
    private static final LuckPerms luckPermsAPI = LuckPermsProvider.get();
    private final ProxyServer server;

    public PlayerLeaveEvent(ProxyServer server) { this.server = server; }

    @Subscribe
    public void onPlayerLeave(DisconnectEvent event) {
        // Check if the disconnect event was successful, this will prevent players who weren't fully on the server to trigger the event
        if (!event.getLoginStatus().equals(DisconnectEvent.LoginStatus.SUCCESSFUL_LOGIN)) return;

        // Check if the player even was on a server
        if (event.getPlayer().getCurrentServer().isEmpty()) return;

        // Get the players prefix
        User user = luckPermsAPI.getUserManager().getUser(event.getPlayer().getUniqueId());
        String prefix = Objects.requireNonNull(user).getCachedData().getMetaData().getPrefix();

        server.sendMessage(MessageManager.leave(prefix));
    }
}
