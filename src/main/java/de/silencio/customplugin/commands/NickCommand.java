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
import org.checkerframework.checker.units.qual.Prefix;
import org.jetbrains.annotations.NotNull;

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

    public NickCommand(ProxyServer server) { this.server = server; }

    @Override
    public void execute(final Invocation invocation) {
        Player player = (Player) invocation.source();

        if (invocation.arguments().length == 0) {
            // Get the luckperms user
            User user = luckPermsAPI.getUserManager().getUser(player.getUniqueId());

            // Remove all prefixes
            clearPrefix(user);

            // Add the players username as prefix
            user.data().add(PrefixNode.builder(player.getUsername(), 100).build());

            // Save the user
            luckPermsAPI.getUserManager().saveUser(user);

        } else if (invocation.arguments().length == 1) {
            // Get the luckperms user
            User user = luckPermsAPI.getUserManager().getUser(player.getUniqueId());

            // Remove all prefixes
            clearPrefix(user);

            // Add the argument as prefix
            user.data().add(PrefixNode.builder(invocation.arguments()[0], 100).build());

            // Save the user
            luckPermsAPI.getUserManager().saveUser(user);

        } else if (invocation.arguments().length == 2) {

            // Check if the player has the permission to change other players nicknames
            if (!invocation.source().hasPermission("custom.nickother") || invocation.source().hasPermission("custom.*")) {
                invocation.source().sendMessage(invalidPermission);
                return;
            }

            // Get the luckperms user
            User user = luckPermsAPI.getUserManager().getUser(invocation.arguments()[0]);

            // Remove all prefixes
            clearPrefix(user);

            // Add the argument as prefix
            user.data().add(PrefixNode.builder(invocation.arguments()[1], 100).build());

            // Save the user
            luckPermsAPI.getUserManager().saveUser(user);
        } else invocation.source().sendMessage(invalidUsage);
    }

    private void clearPrefix(User user) {
        // Get all prefixes
        Set<PrefixNode> prefixNodeSet = Objects.requireNonNull(user).getNodes().stream()
                .filter(NodeType.PREFIX::matches)
                .map(NodeType.PREFIX::cast)
                .collect(Collectors.toSet());

        // Remove all prefixes
        for (PrefixNode node : prefixNodeSet) { user.data().remove(node); }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        List<String> returnList = new ArrayList<>(List.of("<Nickname>"));
        if (invocation.source().hasPermission("custom.nickother") || invocation.source().hasPermission("custom.*")) {
            for (Player p : server.getAllPlayers()) returnList.add(p.getUsername());
        }
        return CompletableFuture.completedFuture(returnList);
    }
}
