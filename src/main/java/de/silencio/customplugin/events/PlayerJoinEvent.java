package de.silencio.customplugin.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PrefixNode;

import java.util.Objects;

public class PlayerJoinEvent {
    private static final LuckPerms luckPermsAPI = LuckPermsProvider.get();
    private static final MiniMessage mm = MiniMessage.miniMessage();
    private final ProxyServer server;

    public PlayerJoinEvent(ProxyServer server) { this.server = server; }

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        // Set the players prefix to their username if it is not set
        User user = luckPermsAPI.getUserManager().getUser(event.getPlayer().getUniqueId());
        String prefix = Objects.requireNonNull(user).getCachedData().getMetaData().getPrefix();
        if (prefix == null) {
            // This means the player logged in for the first time, set their prefix to their username. This prefix is used for nicknames.
            user.data().add(PrefixNode.builder(event.getPlayer().getUsername(), 100).build());
            luckPermsAPI.getUserManager().saveUser(user);
        }

        // Send player logged in message globally
        final Component playerJoinMessage = mm.deserialize("<dark_gray>[<dark_green>+<dark_gray>]<reset> " + prefix);
        server.sendMessage(playerJoinMessage);
    }
}
