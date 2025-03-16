package de.silencio.customplugin.commands;

import com.google.common.base.Joiner;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.silencio.customplugin.CustomPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements SimpleCommand {

    private final ProxyServer server;
    private final CustomPlugin customPlugin;
    private final List<String> commandList = new ArrayList<>();
    private static final MiniMessage mm = MiniMessage.miniMessage();

    final static Component header = mm.deserialize("<gold>These are all registered command, including aliases:");
    final static Component invalidPermission = mm.deserialize("<red>You don't have permission to use this command.");

    public HelpCommand(ProxyServer server, CustomPlugin customPlugin) {
        this.server = server;
        this.customPlugin = customPlugin;
    }

    @Override
    public void execute(final Invocation invocation) {
        if (!invocation.source().hasPermission("custom.help") || !invocation.source().hasPermission("custom.*")) {
            invocation.source().sendMessage(invalidPermission);
            return;
        }

        invocation.source().sendMessage(header);

        for (String alias : server.getCommandManager().getAliases()) {
            if (server.getCommandManager().getCommandMeta(alias).getPlugin() == customPlugin) { commandList.add(alias); }
        }

        final Component message = mm.deserialize(Joiner.on(", ").join(commandList));
        invocation.source().sendMessage(message);
    }
}
