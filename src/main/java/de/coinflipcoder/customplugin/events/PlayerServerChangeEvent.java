package de.coinflipcoder.customplugin.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.coinflipcoder.customplugin.managers.MessageManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.util.Objects;

public class PlayerServerChangeEvent {
    private static final LuckPerms luckPermsAPI = LuckPermsProvider.get();
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

        server.sendMessage(MessageManager.switchServer(prefix, event.getServer().getServerInfo().getName()));
    }
}
