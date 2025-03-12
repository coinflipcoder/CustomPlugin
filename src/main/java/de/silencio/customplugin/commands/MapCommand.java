package de.silencio.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class MapCommand implements SimpleCommand {
    static MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public void execute(final Invocation invocation) {
        Component mapMessage = mm.deserialize("<aqua>The map can be viewed at <gold>https://map.devlencio.net</gold><aqua>.");
        invocation.source().sendMessage(mapMessage);
    }
}
