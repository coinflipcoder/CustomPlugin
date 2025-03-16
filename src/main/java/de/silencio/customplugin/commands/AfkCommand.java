package de.silencio.customplugin.commands;

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.SuffixNode;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class AfkCommand implements RawCommand {
    private final ProxyServer server;
    private static final LuckPerms luckPermsAPI = LuckPermsProvider.get();
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public AfkCommand(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(final Invocation invocation) {
        Player player = (Player) invocation.source();
        User user = luckPermsAPI.getUserManager().getUser(player.getUniqueId());

        String userPrefix = Objects.requireNonNull(user).getCachedData().getMetaData().getPrefix();
        String result = userPrefix.replaceAll("&#[0-9A-Fa-f]{6}|&[0-9a-fA-Fk-oK-OrR]", "");

        Set<SuffixNode> suffixNodeSet = Objects.requireNonNull(user).getNodes().stream()
                .filter(NodeType.SUFFIX::matches)
                .map(NodeType.SUFFIX::cast)
                .collect(Collectors.toSet());

        if (suffixNodeSet.isEmpty()) {
            // player is not afk
            user.data().add(SuffixNode.builder(" &8[AFK]", 100).build());
            luckPermsAPI.getUserManager().saveUser(user);

            Component message = mm.deserialize("<gray><italic>" + result + " is now AFK.");
            server.sendMessage(message);
        } else {
            // player is afk
            for (SuffixNode node : suffixNodeSet) { user.data().remove(node); }
            luckPermsAPI.getUserManager().saveUser(user);
            Component message = mm.deserialize("<gray><italic>" + result + " is no longer AFK.");
            server.sendMessage(message);
        }
    }
}