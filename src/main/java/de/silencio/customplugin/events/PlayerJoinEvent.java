package de.silencio.customplugin.events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PrefixNode;

import java.util.Objects;

public class PlayerJoinEvent {
    private static final LuckPerms luckPermsAPI = LuckPermsProvider.get();

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        User user = luckPermsAPI.getUserManager().getUser(event.getPlayer().getUniqueId());
        String prefix = Objects.requireNonNull(user).getCachedData().getMetaData().getPrefix();
        if (prefix == null) {
            user.data().add(PrefixNode.builder(event.getPlayer().getUsername(), 100).build());
            luckPermsAPI.getUserManager().saveUser(user);
        }
    }
}
