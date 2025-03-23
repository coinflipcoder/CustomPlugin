package de.silencio.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class MapCommand implements SimpleCommand {
    private static final MiniMessage mm = MiniMessage.miniMessage();

    final static Component mapMessage = mm.deserialize("<aqua>The map can be viewed <gold><u><hover:show_text:'<gold>Click me!'><click:open_url:'https://map.devlencio.net'>here</click></hover></u></gold><aqua>.");

    @Override
    public void execute(final Invocation invocation) { invocation.source().sendMessage(mapMessage); }
}
