package de.silencio.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class InfoCommand implements SimpleCommand {
    private static final MiniMessage mm = MiniMessage.miniMessage();

    final static Component emptyLine = Component.text("");
    final static Component invalidUsage = mm.deserialize("<red>Invalid command usage.");
    final static Component invalidServer = mm.deserialize("<red>No info for this server.");

    @Override
    public void execute(final Invocation invocation) {
        Player player = (Player) invocation.source();

        switch (player.getCurrentServer().get().getServerInfo().getName()) {
            case "lobby":
                Component headerLobby = mm.deserialize("<aqua>Hello! Welcome to the network.");
                Component lobbyCommand = mm.deserialize("<gray>This is the lobby. You can return here anytime using <gold>/hub <gray>or <gold>/lobby<gray>.");
                Component nickCommand = mm.deserialize("<gray>Use <gold>/nick <nickname> <gray>to change your displayed name across the network. You may use colors with the format <aqua>&#ffffff <gray>or <aqua>&6.");
                Component infoCommand = mm.deserialize("<gray>This command is different for every server! Try it on the survival server!");

                invocation.source().sendMessage(emptyLine);
                invocation.source().sendMessage(headerLobby);
                invocation.source().sendMessage(emptyLine);
                invocation.source().sendMessage(lobbyCommand);
                invocation.source().sendMessage(emptyLine);
                invocation.source().sendMessage(nickCommand);
                invocation.source().sendMessage(infoCommand);
                return;
            case "survival":
                if (invocation.arguments().length == 0) {
                    Component header = mm.deserialize("<aqua>Hello! Welcome to the Survival Server. Here is some useful information about it.");
                    Component disclaimer = mm.deserialize("<gray>This server is using Fabric to run a multitude of server side mods. No mods are needed for you to play.");
                    Component sections = mm.deserialize("<aqua>Use these commands to view individual sections:");
                    Component commands = mm.deserialize(" <gray>/info</gray> <gold>commands:</gold> <gray>-> Information commands.");
                    Component changes = mm.deserialize("  <gray>/info</gray> <gold>changes:</gold> <gray>-> Information about small changes and QOL tweaks.");
                    Component crafting = mm.deserialize(" <gray>/info</gray> <gold>crafting:</gold> <gray>-> Information about new and altered crafting recipes.");

                    invocation.source().sendMessage(emptyLine);
                    invocation.source().sendMessage(header);
                    invocation.source().sendMessage(emptyLine);
                    invocation.source().sendMessage(disclaimer);
                    invocation.source().sendMessage(emptyLine);
                    invocation.source().sendMessage(sections);
                    invocation.source().sendMessage(commands);
                    invocation.source().sendMessage(changes);
                    invocation.source().sendMessage(crafting);
                    return;
                } else {
                    switch (invocation.arguments()[0]) {
                        case "changes":
                            Component changes = mm.deserialize("<bold><gold>Changes:</gold></bold>\n" +
                                    " <bold><blue>»</blue></bold> <gray> Axes can be used as weapons. They also support the Sharpness enchantment.\n" +
                                    " <bold><blue>»</blue></bold> <gray> Sneaking twice whilst looking down allows you to sit anywhere.\n" +
                                    " <bold><blue>»</blue></bold> <gray> Right clicking a crop with a hoe automatically harvests and replants it.\n" +
                                    " <bold><blue>»</blue></bold> <gray> Crop Trampling is disabled.\n" +
                                    " <bold><blue>»</blue></bold> <gray> Timber is installed. Shift to cut down the entire tree.\n" +
                                    " <bold><blue>»</blue></bold> <gray> Concrete powder can be converted when dropped into a water filled cauldron.\n" +
                                    " <bold><blue>»</blue></bold> <gray> Nether portals can have any shape or size.\n" +
                                    " <bold><blue>»</blue></bold> <gray> Mobs can be silenced using a name tag called \"silence me\".\n" +
                                    " <bold><blue>»</blue></bold> <gray> Revamped anvil xp costs.\n" +
                                    " <bold><blue>»</blue></bold> <gray> The height limit is 2032 blocks.\n" +
                                    " <bold><blue>»</blue></bold> <gray> You dont need an arrow in your inventory to use infinity.\n" +
                                    " <bold><blue>»</blue></bold> <gray> Infinity and mending are combinable.\n" +
                                    " <bold><blue>»</blue></bold> <gray> Villagers follow you when holding emeralds.\n" +
                                    " <bold><blue>»</blue></bold> <gray> Villager discounts are shared between players.\n");
                            invocation.source().sendMessage(emptyLine);
                            invocation.source().sendMessage(changes);
                            return;
                        case "crafting":
                            Component crafting = mm.deserialize("<bold><gold>Crafting Tweaks:</gold></bold>\n" +
                                    " <bold><blue>»</blue></bold> <gray> Ice can be unpacked into its former items.\n" +
                                    " <bold><blue>»</blue></bold> <gray> Increased the amount of trapdoors, bark, stairs, bricks and carpets crafted.\n" +
                                    " <bold><blue>»</blue></bold> <gray> Coal can be crafted into black dye.\n" +
                                    " <bold><blue>»</blue></bold> <gray> Many recipes like bread, paper or shulker boxes are now shapeless.\n" +
                                    " <bold><blue>»</blue></bold> <gray> You can dye blocks with already dyed blocks.\n" +
                                    " <bold><blue>»</blue></bold> <gray> Sandstone and its variants can be dyed into their red variant using red dye.\n" +
                                    " <bold><blue>»</blue></bold> <gray> Blackstone can now replace cobblestone in any recipe.\n" +
                                    " <bold><blue>»</blue></bold> <gray> Concrete powder can be smelted into colored glass.");
                            invocation.source().sendMessage(emptyLine);
                            invocation.source().sendMessage(crafting);
                            return;
                        case "commands":
                            Component commands = mm.deserialize("<bold><gold>Commands:</gold></bold>\n" +
                                    " <bold><blue>»</blue></bold> <gray> /home set <name> -> Set an new home. You can set up to three.\n" +
                                    " <bold><blue>»</blue></bold> <gray> /home tp <name> -> Teleport to a home.\n" +
                                    " <bold><blue>»</blue></bold> <gray> /home delete <name> -> Removes a home.\n" +
                                    " <bold><blue>»</blue></bold> <gray> /tpa <player> -> Request to teleport to someone.\n" +
                                    " <bold><blue>»</blue></bold> <gray> /afk -> Sets your status to AFK.\n" +
                                    " <bold><blue>»</blue></bold> <gray> /nick <nickname> -> Changes your displayed name.\n" +
                                    " <bold><blue>»</blue></bold> <gray> /map -> Link to the browser map.");
                            invocation.source().sendMessage(emptyLine);
                            invocation.source().sendMessage(commands);
                            return;
                        default:
                            invocation.source().sendMessage(invalidUsage);
                            return;
                    }
                }
            default: invocation.source().sendMessage(invalidServer);
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
