package de.silencio.customplugin.commands;

import com.google.common.base.Joiner;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Arrays;

public class SayCommand implements SimpleCommand {

    private final ProxyServer server;
    static MiniMessage mm = MiniMessage.miniMessage();

    public SayCommand(ProxyServer server) { this.server = server; }

    @Override
    public void execute(final Invocation invocation) {
        if (invocation.source().hasPermission("custom.say") || invocation.source().hasPermission("custom.*")) {
            if (invocation.arguments().length >= 1) {
                Component message = mm.deserialize(Joiner.on(" ").join(invocation.arguments()));

                for (RegisteredServer registeredServer : server.getAllServers()) {
                    registeredServer.sendMessage(message);
                }
            }
        }
    }
}
