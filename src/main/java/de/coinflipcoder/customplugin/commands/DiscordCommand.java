package de.coinflipcoder.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import de.coinflipcoder.customplugin.managers.MessageManager;

public final class DiscordCommand implements SimpleCommand {

    @Override
    public void execute(final Invocation invocation) { invocation.source().sendMessage(MessageManager.DISCORD_MESSAGE); }
}
