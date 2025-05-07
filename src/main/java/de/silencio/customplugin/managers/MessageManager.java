package de.silencio.customplugin.managers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class MessageManager {
    private static final MiniMessage mm = MiniMessage.miniMessage();

    // Global
    public static final Component INVALID_PERMISSION = mm.deserialize("<red>You don't have permission to use this command.");
    public static final Component INVALID_USAGE = mm.deserialize("<red>Invalid command usage.");
    public static final Component INVALID_PLAYER = mm.deserialize("<red>Player not found.");
    public static final Component INVALID_SERVER = mm.deserialize("<red>Invalid server.");
    public static final Component EMPTY = Component.text("");

    // AFK
    private static final String NOW_AFK_TEMPLATE = "<gray><italic><name> is now AFK.";
    private static final String NOT_AFK_TEMPLATE = "<gray><italic><name> is no longer AFK.";

    // Chat
    private static final String JOIN_MESSAGE_TEMPLATE = "<dark_gray>[<dark_green>+<dark_gray>]<reset> <name>";
    private static final String LEAVE_MESSAGE_TEMPLATE = "<dark_gray>[<red>-<dark_gray>]<reset> <name>";
    private static final String CHAT_MESSAGE_TEMPLATE = "<dark_gray>[<dark_aqua><server><dark_gray>]<reset> <name><dark_gray>:<reset> <message>";
    private static final String SWITCH_SERVER_TEMPLATE = "<dark_gray>[<aqua>➟<dark_gray>]<reset> <name> <dark_gray>joined <dark_aqua><server>";

    // Playtime
    public static final String PLAYTIME_TEMPLATE = "<aqua>Total Playtime: <gold><total><aqua><newline>Current Playtime: <gold><current>";
    public static final Component PLAYTIME_RELOAD = mm.deserialize("<gold>Playtime has been reloaded.");

    // Ban Manager
    public static final Component BAN_MESSAGE = mm.deserialize("<red>You are banned from the network.");
    public static final Component KICK_MESSAGE = mm.deserialize("<red>You have been kicked from the server.");
    private static final String BANNED_TEMPLATE = "<green>Successfully banned <name>.";
    private static final String UNBANNED_TEMPLATE = "<green>Successfully unbanned <name>.";
    public static final Component NOT_BANNED = mm.deserialize("<red>Player is not banned.");
    public static final Component ALREADY_BANNED = mm.deserialize("<red>Player is already banned.");

    // Commands
    public static final Component MAP_MESSAGE = mm.deserialize("<aqua>The map can be viewed <gold><u><hover:show_text:'<gold>Click me!'><click:open_url:'https://map.devlencio.net'>here</click></hover></u></gold><aqua>.");
    public static final Component DISCORD_MESSAGE = mm.deserialize("<aqua>Join the discord server <gold><u><hover:show_text:'<gold>Click me!'><click:open_url:'https://discord.devlencio.net'>here</click></hover></u></gold><aqua>.");
    public static final Component NO_INFO = mm.deserialize("<red>No info for this server.");
    public static final Component HELP_HEADER = mm.deserialize("<gold>These are all registered command, including aliases:");

    // Info Command
    public static final Component FABRIC_DISCLAIMER = mm.deserialize("<gray>This server is using Fabric to run a multitude of server side mods. No mods are needed for you to play.");
    public static final Component SURVIVAL_CHANGES = mm.deserialize("""
                                    <bold><gold>Changes:</gold></bold>
                                     <bold><blue>»</blue></bold> <gray> Axes support the Sharpness enchantment.
                                     <bold><blue>»</blue></bold> <gray> Sneaking twice whilst looking down allows you to sit anywhere.
                                     <bold><blue>»</blue></bold> <gray> Right clicking a crop with a hoe automatically harvests and replants it.
                                     <bold><blue>»</blue></bold> <gray> Crop Trampling is disabled.
                                     <bold><blue>»</blue></bold> <gray> Timber is installed. Shift to cut down the entire tree.
                                     <bold><blue>»</blue></bold> <gray> Concrete powder can be converted when dropped into a water filled cauldron.
                                     <bold><blue>»</blue></bold> <gray> Dirt can be converted to mud when dropped into a water filled cauldron.
                                     <bold><blue>»</blue></bold> <gray> Nether portals can have any shape or size.
                                     <bold><blue>»</blue></bold> <gray> Mobs can be silenced using a name tag called "silence me".
                                     <bold><blue>»</blue></bold> <gray> Revamped anvil xp costs.
                                     <bold><blue>»</blue></bold> <gray> The height limit is 2032 blocks.
                                     <bold><blue>»</blue></bold> <gray> You don't need an arrow in your inventory to use infinity.
                                     <bold><blue>»</blue></bold> <gray> Infinity and mending are combinable.
                                     <bold><blue>»</blue></bold> <gray> Villagers follow you when holding emeralds.
                                     <bold><blue>»</blue></bold> <gray> Villager discounts are shared between players.
                                     <bold><blue>»</blue></bold> <gray> Husks drop sand.
                                     <bold><blue>»</blue></bold> <gray> Players and mobs drop their heads when killed.
                                     <bold><blue>»</blue></bold> <gray> Budding Amethysts can be mined with silk touch.
                                     <bold><blue>»</blue></bold> <gray> Beacon base range is 50, with 25 per level and infinite range below.
                                    """);
    public static final Component SURVIVAL_CRAFTING = mm.deserialize("""
                                    <bold><gold>Crafting Tweaks:</gold></bold>
                                     <bold><blue>»</blue></bold> <gray> Ice can be unpacked into its former items.
                                     <bold><blue>»</blue></bold> <gray> Increased the amount of trapdoors, bark, stairs, bricks and carpets crafted.
                                     <bold><blue>»</blue></bold> <gray> Coal and Charcoal can be crafted into black dye.
                                     <bold><blue>»</blue></bold> <gray> Many recipes like bread, paper or shulker boxes are now shapeless.
                                     <bold><blue>»</blue></bold> <gray> You can dye blocks with already dyed blocks.
                                     <bold><blue>»</blue></bold> <gray> Sandstone and its variants can be dyed into their red variant using red dye.
                                     <bold><blue>»</blue></bold> <gray> Blackstone can now replace cobblestone in any recipe.
                                     <bold><blue>»</blue></bold> <gray> Individual paintings can be selected in a stonecutter.
                                     <bold><blue>»</blue></bold> <gray> Mini blocks can be crafted in a stonecutter.
                                     <bold><blue>»</blue></bold> <gray> Concrete powder can be smelted into colored glass.
                                    """);
    public static final Component SURVIVAL_COMMANDS = mm.deserialize("""
                                    <bold><gold>Commands:</gold></bold>
                                     <bold><blue>»</blue></bold> <gray> /home set <name> -> Set an new home. You can set up to three.
                                     <bold><blue>»</blue></bold> <gray> /home tp <name> -> Teleport to a home.
                                     <bold><blue>»</blue></bold> <gray> /home delete <name> -> Removes a home.
                                     <bold><blue>»</blue></bold> <gray> /tpa <player> -> Request to teleport to someone.
                                     <bold><blue>»</blue></bold> <gray> /afk -> Sets your status to AFK.
                                     <bold><blue>»</blue></bold> <gray> /nick <nickname> -> Changes your displayed name.
                                     <bold><blue>»</blue></bold> <gray> /playtime -> View your total time played.
                                     <bold><blue>»</blue></bold> <gray> /map -> Link to the browser map.
                                     <bold><blue>»</blue></bold> <gray> /discord -> Link to the discord server.
                                    """);

    public static final Component CREATIVE_COMMANDS = mm.deserialize("""
                                    <bold><gold>Commands:</gold></bold>
                                     <bold><blue>»</blue></bold> <gray> /gmc -> Creative mode.
                                     <bold><blue>»</blue></bold> <gray> /gms -> Survival mode.
                                     <bold><blue>»</blue></bold> <gray> /gma -> Adventure mode.
                                     <bold><blue>»</blue></bold> <gray> /gmsp -> Spectator mode.
                                     <bold><blue>»</blue></bold> <gray> /overworld -> Teleport to the overworld.
                                     <bold><blue>»</blue></bold> <gray> /nether -> Teleport to the nether.
                                     <bold><blue>»</blue></bold> <gray> /end -> Teleport to the end.
                                     <bold><blue>»</blue></bold> <gray> /rename <name> -> Renames your held item.
                                     \s
                                     <bold><blue>»</blue></bold> <gray> All of Worldedit is available.
                                     <bold><blue>»</blue></bold> <gray> All of Axiom is available.
                                    """);

    public static Component nowAfk(String player) {
        return mm.deserialize(NOW_AFK_TEMPLATE, Placeholder.parsed("name", player));
    }

    public static Component notAfk(String player) {
        return mm.deserialize(NOT_AFK_TEMPLATE, Placeholder.parsed("name", player));
    }

    public static Component banSuccess(String player) {
        return mm.deserialize(BANNED_TEMPLATE, Placeholder.parsed("name", player));
    }

    public static Component unbanSuccess(String player) {
        return mm.deserialize(UNBANNED_TEMPLATE, Placeholder.parsed("name", player));
    }

    public static Component join(String player) {
        return mm.deserialize(JOIN_MESSAGE_TEMPLATE, Placeholder.parsed("name", player));
    }

    public static Component leave(String player) {
        return mm.deserialize(LEAVE_MESSAGE_TEMPLATE, Placeholder.parsed("name", player));
    }

    public static Component chat(String player, String server, String message) {
        return mm.deserialize(CHAT_MESSAGE_TEMPLATE,
                Placeholder.parsed("server", server),
                Placeholder.parsed("name", player),
                Placeholder.parsed("message", message)
        );
    }

    public static Component switchServer(String player, String server) {
        return mm.deserialize(SWITCH_SERVER_TEMPLATE,
                Placeholder.parsed("server", server),
                Placeholder.parsed("name", player)
        );
    }

    public static Component playTime(String total, String current) {
        return mm.deserialize(PLAYTIME_TEMPLATE,
                Placeholder.parsed("total", total),
                Placeholder.parsed("current", current)
        );
    }
}
