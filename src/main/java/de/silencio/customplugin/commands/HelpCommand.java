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
    static MiniMessage mm = MiniMessage.miniMessage();

    public HelpCommand(ProxyServer server, CustomPlugin customPlugin) {
        this.server = server;
        this.customPlugin = customPlugin;
    }

    @Override
    public void execute(final Invocation invocation) {
        if (invocation.source().hasPermission("custom.help") || invocation.source().hasPermission("custom.*")) {
            Component header = mm.deserialize("<gold>These are all registered command, including aliases:");
            invocation.source().sendMessage(header);
            List<String> commandList = new ArrayList<>();
            for (String alias : server.getCommandManager().getAliases()) {
                if (server.getCommandManager().getCommandMeta(alias).getPlugin() == customPlugin) {
                    commandList.add(alias);
                }
            }
            Component message = mm.deserialize(Joiner.on(", ").join(commandList));
            invocation.source().sendMessage(message);
        }
    }
}
