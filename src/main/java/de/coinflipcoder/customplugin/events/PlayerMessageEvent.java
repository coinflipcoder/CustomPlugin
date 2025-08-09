package de.coinflipcoder.customplugin.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import de.coinflipcoder.customplugin.managers.MessageManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.util.Objects;

public class PlayerMessageEvent {
    private static final LuckPerms luckPermsAPI = LuckPermsProvider.get();
    private final ProxyServer server;

    public PlayerMessageEvent(ProxyServer server) { this.server = server; }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        // Cancel the event to prevent the message from being sent twice
        event.setResult(PlayerChatEvent.ChatResult.denied());

        // Get the players prefix
        User user = luckPermsAPI.getUserManager().getUser(event.getPlayer().getUniqueId());
        String prefix = Objects.requireNonNull(user).getCachedData().getMetaData().getPrefix();

        // Get the server name or set it to "Unknown"
        String serverName = event.getPlayer().getCurrentServer().get().getServer().getServerInfo().getName();
        if (serverName == null) serverName = "Unknown";

        // Send player chat message globally
        server.sendMessage(MessageManager.chat(prefix, serverName, event.getMessage()));
    }
}
