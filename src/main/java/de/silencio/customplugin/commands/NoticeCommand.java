package de.silencio.customplugin.commands;

import com.velocitypowered.api.command.SimpleCommand;
import de.silencio.customplugin.CustomPlugin;
import de.silencio.customplugin.managers.MessageManager;
import de.silencio.customplugin.managers.NoticeManager;

public class NoticeCommand implements SimpleCommand {
    private final NoticeManager noticeManager;

    public NoticeCommand(CustomPlugin plugin) {
        this.noticeManager = plugin.getNoticeManager();
    }

    @Override
    public void execute(final Invocation invocation) {
        // Check if the player has the permission to use this command
        if (!invocation.source().hasPermission("custom.notice") || !invocation.source().hasPermission("custom.*")) {
            invocation.source().sendMessage(MessageManager.INVALID_PERMISSION);
            return;
        }

        // Clear the notice if no arguments are provided
        if (invocation.arguments().length == 0) {
            noticeManager.clearNotice();
            invocation.source().sendMessage(MessageManager.CLEAR_NOTICE);
        } else {
            // construct string from arguments
            StringBuilder notice = new StringBuilder();
            for (String arg : invocation.arguments()) {
                notice.append(arg).append(" ");
            }

            // Set the notice
            noticeManager.setNotice(notice.toString().trim());
            invocation.source().sendMessage(MessageManager.SET_NOTICE);
        }
    }
}
