package de.silencio.customplugin.commands;

import com.google.common.base.Joiner;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import de.silencio.customplugin.CustomPlugin;
import de.silencio.customplugin.managers.MessageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements SimpleCommand {

    private final ProxyServer server;
    private final CustomPlugin plugin;
    private final List<String> commandList = new ArrayList<>();
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public HelpCommand(ProxyServer server, CustomPlugin plugin) {
        this.server = server;
        this.plugin = plugin;
    }

    @Override
    public void execute(final Invocation invocation) {
        if (!invocation.source().hasPermission("custom.help") || !invocation.source().hasPermission("custom.*")) {
            invocation.source().sendMessage(MessageManager.INVALID_PERMISSION);
            return;
        }

        invocation.source().sendMessage(MessageManager.HELP_HEADER);

        for (String alias : server.getCommandManager().getAliases()) {
            if (server.getCommandManager().getCommandMeta(alias).getPlugin() == plugin) { commandList.add(alias); }
        }

        final Component message = mm.deserialize(Joiner.on(", ").join(commandList));
        invocation.source().sendMessage(message);
    }
}
