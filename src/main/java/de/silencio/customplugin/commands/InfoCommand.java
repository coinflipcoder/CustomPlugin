package de.silencio.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import de.silencio.customplugin.managers.MessageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class InfoCommand implements SimpleCommand {
    private static final MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public void execute(final Invocation invocation) {
        Player player = (Player) invocation.source();

        switch (player.getCurrentServer().get().getServerInfo().getName()) {
            case "lobby":
                Component headerLobby = mm.deserialize("<aqua>Hello! Welcome to the network.");
                Component lobbyCommand = mm.deserialize("<gray>This is the lobby. You can return here anytime using <gold>/hub <gray>or <gold>/lobby<gray>.");
                Component nickCommand = mm.deserialize("<gray>Use <gold><hover:show_text:'<gold>Click me!'><click:SUGGEST_COMMAND:/nick >/nick <nickname></click></hover> <gray>to change your displayed name across the network. This uses the <gold><u><hover:show_text:'<gold>Click me!'><click:open_url:'https://docs.advntr.dev/minimessage/format.html'>MiniMessage</click></hover></u><gray> format. <gold><u><hover:show_text:'<gold>Click me!'><click:open_url:'https://www.birdflop.com/resources/rgb/'>This</click></hover></u><gray> site can help you with that, set Color Format to 'MiniMessage'.");
                Component infoCommand = mm.deserialize("<gray>This command is different for every server! Try it on the survival server!");

                invocation.source().sendMessage(MessageManager.EMPTY);
                invocation.source().sendMessage(headerLobby);
                invocation.source().sendMessage(MessageManager.EMPTY);
                invocation.source().sendMessage(lobbyCommand);
                invocation.source().sendMessage(MessageManager.EMPTY);
                invocation.source().sendMessage(nickCommand);
                invocation.source().sendMessage(infoCommand);
                return;
            case "survival":
                if (invocation.arguments().length == 0) {
                    Component header = mm.deserialize("<aqua>Hello! Welcome to the Survival Server. Here is some useful information about it.");
                    Component sections = mm.deserialize("<aqua>Use these commands to view individual sections:");
                    Component commands = mm.deserialize(" <gray>/info</gray> <gold>commands<gray>: -> Information commands.");
                    Component changes = mm.deserialize("  <gray>/info</gray> <gold>changes<gray>: -> Information about small changes and QOL tweaks.");
                    Component crafting = mm.deserialize(" <gray>/info</gray> <gold>crafting<gray>: -> Information about new and altered crafting recipes.");

                    invocation.source().sendMessage(MessageManager.EMPTY);
                    invocation.source().sendMessage(header);
                    invocation.source().sendMessage(MessageManager.EMPTY);
                    invocation.source().sendMessage(MessageManager.FABRIC_DISCLAIMER);
                    invocation.source().sendMessage(MessageManager.EMPTY);
                    invocation.source().sendMessage(sections);
                    invocation.source().sendMessage(commands);
                    invocation.source().sendMessage(changes);
                    invocation.source().sendMessage(crafting);
                    return;
                } else {
                    switch (invocation.arguments()[0]) {
                        case "changes":
                            invocation.source().sendMessage(MessageManager.EMPTY);
                            invocation.source().sendMessage(MessageManager.SURVIVAL_CHANGES);
                            return;
                        case "crafting":
                            invocation.source().sendMessage(MessageManager.EMPTY);
                            invocation.source().sendMessage(MessageManager.SURVIVAL_CRAFTING);
                            return;
                        case "commands":
                            invocation.source().sendMessage(MessageManager.EMPTY);
                            invocation.source().sendMessage(MessageManager.SURVIVAL_COMMANDS);
                            return;
                        default:
                            invocation.source().sendMessage(MessageManager.INVALID_USAGE);
                            return;
                    }
                }
            case "creative":
                Component headerCreative = mm.deserialize("<aqua>Hello! Welcome to the Creative Server. Here is some useful information about it.");

                invocation.source().sendMessage(MessageManager.EMPTY);
                invocation.source().sendMessage(headerCreative);
                invocation.source().sendMessage(MessageManager.EMPTY);
                invocation.source().sendMessage(MessageManager.FABRIC_DISCLAIMER);
                invocation.source().sendMessage(MessageManager.EMPTY);
                invocation.source().sendMessage(MessageManager.CREATIVE_COMMANDS);
                return;
            default: invocation.source().sendMessage(MessageManager.NO_INFO);
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        Player p = (Player) invocation.source();

        if (p.getCurrentServer().get().getServerInfo().getName().equals("survival")) {
            return CompletableFuture.completedFuture(List.of("commands", "changes", "crafting"));
        }

        return CompletableFuture.completedFuture(List.of());
    }
}
