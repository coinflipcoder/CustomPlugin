package de.silencio.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class NickCommand implements SimpleCommand {
    private static final LuckPerms luckPermsAPI = LuckPermsProvider.get();
    static MiniMessage mm = MiniMessage.miniMessage();
    static Component invalidUsage = mm.deserialize("<red>Invalid command usage.");
    static Component invalidPermission = mm.deserialize("<red>You don't have permission to use this command.");
    private final ProxyServer server;

    public NickCommand(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(final Invocation invocation) {
        Player player = (Player) invocation.source();

        if (invocation.arguments().length == 0) {
            User user = luckPermsAPI.getUserManager().getUser(player.getUniqueId());
            Set<PrefixNode> prefixNodeSet = Objects.requireNonNull(user).getNodes().stream()
                    .filter(NodeType.PREFIX::matches)
                    .map(NodeType.PREFIX::cast)
                    .collect(Collectors.toSet());
            for (PrefixNode node : prefixNodeSet) { user.data().remove(node); }
            user.data().add(PrefixNode.builder(player.getUsername(), 100).build());
            luckPermsAPI.getUserManager().saveUser(user);

        } else if (invocation.arguments().length == 1) {
            User user = luckPermsAPI.getUserManager().getUser(player.getUniqueId());
            Set<PrefixNode> prefixNodeSet = Objects.requireNonNull(user).getNodes().stream()
                    .filter(NodeType.PREFIX::matches)
                    .map(NodeType.PREFIX::cast)
                    .collect(Collectors.toSet());
            for (PrefixNode node : prefixNodeSet) { user.data().remove(node); }
            user.data().add(PrefixNode.builder(invocation.arguments()[0], 100).build());
            luckPermsAPI.getUserManager().saveUser(user);

        } else if (invocation.arguments().length == 2) {
            if (!invocation.source().hasPermission("custom.nickother")) {
                invocation.source().sendMessage(invalidPermission);
                return;
            }

            User user = luckPermsAPI.getUserManager().getUser(invocation.arguments()[0]);
            Set<PrefixNode> prefixNodeSet = Objects.requireNonNull(user).getNodes().stream()
                    .filter(NodeType.PREFIX::matches)
                    .map(NodeType.PREFIX::cast)
                    .collect(Collectors.toSet());
            for (PrefixNode node : prefixNodeSet) { user.data().remove(node); }
            user.data().add(PrefixNode.builder(invocation.arguments()[1], 100).build());
            luckPermsAPI.getUserManager().saveUser(user);
        } else invocation.source().sendMessage(invalidUsage);
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        List<String> returnList = new ArrayList<>(List.of("<Nickname>"));
        if (invocation.source().hasPermission("custom.nickother")) {
            for (Player p : server.getAllPlayers()) {
                returnList.add(p.getUsername());
            }
        }
        return CompletableFuture.completedFuture(returnList);
    }
}
