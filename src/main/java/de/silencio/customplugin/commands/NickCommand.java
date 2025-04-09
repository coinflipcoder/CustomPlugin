package de.silencio.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.silencio.customplugin.managers.MessageManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class NickCommand implements SimpleCommand {
    private final ProxyServer server;
    private static final LuckPerms luckPermsAPI = LuckPermsProvider.get();

    public NickCommand(ProxyServer server) { this.server = server; }

    @Override
    public void execute(final Invocation invocation) {
        Player player = (Player) invocation.source();

        // Reset the players nickname
        if (invocation.arguments().length == 0) {
            // Get the luckperms user
            User user = luckPermsAPI.getUserManager().getUser(player.getUniqueId());

            // Remove all prefixes
            clearPrefix(user);

            // Add the players username as prefix
            user.data().add(PrefixNode.builder(player.getUsername(), 100).build());

            // Save the user
            luckPermsAPI.getUserManager().saveUser(user);

        // Set the players nickname
        } else if (invocation.arguments().length == 1) {
            // Get the luckperms user
            User user = luckPermsAPI.getUserManager().getUser(player.getUniqueId());

            // Remove all prefixes
            clearPrefix(user);

            // Add the argument as prefix
            user.data().add(PrefixNode.builder(invocation.arguments()[0], 100).build());

            // Save the user
            luckPermsAPI.getUserManager().saveUser(user);

        // Set another players nickname
        } else if (invocation.arguments().length == 2) {

            // Check if the player has the permission to change other players nicknames
            if (!invocation.source().hasPermission("custom.nickother") || invocation.source().hasPermission("custom.*")) {
                invocation.source().sendMessage(MessageManager.INVALID_PERMISSION);
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
        } else invocation.source().sendMessage(MessageManager.INVALID_USAGE);
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
