package de.coinflipcoder.customplugin.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.coinflipcoder.customplugin.CustomPlugin;
import de.coinflipcoder.customplugin.managers.BanManager;
import de.coinflipcoder.customplugin.managers.MessageManager;
import de.coinflipcoder.customplugin.managers.NoticeManager;
import de.coinflipcoder.customplugin.managers.PlaytimeManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PrefixNode;

import java.util.Objects;

public class PlayerJoinEvent {
    private static final LuckPerms luckPermsAPI = LuckPermsProvider.get();
    private static final MiniMessage mm = MiniMessage.miniMessage();
    private final PlaytimeManager playtimeManager;
    private final NoticeManager noticeManager;
    private final BanManager banManager;
    private final ProxyServer server;

    public PlayerJoinEvent(ProxyServer server, CustomPlugin plugin) {
        this.server = server;
        this.banManager = plugin.getBanManager();
        this.playtimeManager = plugin.getPlaytimeManager();
        this.noticeManager = plugin.getNoticeManager();
    }

    @Subscribe
    public void onLogin(LoginEvent event) {
        if (banManager.isBanned(event.getPlayer().getUniqueId())) {
            event.setResult(LoginEvent.ComponentResult.denied(MessageManager.BAN_MESSAGE));
        }
    }

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        // Get Player
        Player player = event.getPlayer();

        // Set the players prefix to their username if it is not set
        User user = luckPermsAPI.getUserManager().getUser(player.getUniqueId());
        String prefix = Objects.requireNonNull(user).getCachedData().getMetaData().getPrefix();
        if (prefix == null) {
            // This means the player logged in for the first time, set their prefix to their username. This prefix is used for nicknames.
            user.data().add(PrefixNode.builder(player.getUsername(), 100).build());
            luckPermsAPI.getUserManager().saveUser(user);
        }

        // Add the player to the playtime manager
        playtimeManager.startSession(player.getUniqueId());

        // Send player logged in message globally
        server.sendMessage(MessageManager.join(prefix));

        // Check if a notice is set
        if (noticeManager.hasNotice()) {
            // Send the notice to the player
            String notice = noticeManager.getNotice();
            player.sendMessage(mm.deserialize(notice));
        }
    }
}
