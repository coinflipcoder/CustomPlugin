package de.silencio.customplugin.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.util.Objects;

public class PlayerLeaveEvent {
    private static final LuckPerms luckPermsAPI = LuckPermsProvider.get();
    private static final MiniMessage mm = MiniMessage.miniMessage();
    private final ProxyServer server;

    static final Component kickMessage = mm.deserialize("<red>You have been kicked from the server.");

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

        final Component playerLeaveMessage = mm.deserialize("<dark_gray>[<red>-<dark_gray>]<reset> " + prefix);
        server.sendMessage(playerLeaveMessage);
    }
}
