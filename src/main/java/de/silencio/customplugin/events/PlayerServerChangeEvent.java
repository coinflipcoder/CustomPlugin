package de.silencio.customplugin.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.util.Objects;

public class PlayerServerChangeEvent {
    private static final LuckPerms luckPermsAPI = LuckPermsProvider.get();
    private static final MiniMessage mm = MiniMessage.miniMessage();
    private final ProxyServer server;

    public PlayerServerChangeEvent(ProxyServer server) { this.server = server; }

    @Subscribe
    public void onChangeServer(ServerConnectedEvent event) {
        // Get the players prefix
        User user = luckPermsAPI.getUserManager().getUser(event.getPlayer().getUniqueId());
        String prefix = Objects.requireNonNull(user).getCachedData().getMetaData().getPrefix();

        // Check if player just logged in
        final RegisteredServer previousServer = event.getPreviousServer().orElse(null);
        if (previousServer == null) return;

        final Component playerServerChangeMessage = mm.deserialize("<dark_gray>[<aqua>➟<dark_gray>]<reset> " + prefix + " <dark_gray>joined <gold>" + event.getServer().getServerInfo().getName());
        server.sendMessage(playerServerChangeMessage);
    }
}
