package ru.ezuykow.tasksbot.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Commands {

    private final Logger logger = LoggerFactory.getLogger(Commands.class);

    public enum Command {
        START,
        HELP,
        TASKS,
        UNKNOWN
    }

    private final CommandsActions commandsActions;

    public Commands(CommandsActions commandsActions) {
        this.commandsActions = commandsActions;
    }

    public void doCommand(String textCommandWithSlash, long chatId) {
        Command cmd = parseCommand(textCommandWithSlash.substring(1));
        doCommandAction(cmd, chatId);
    }

    private void doCommandAction(Command cmd, long chatId) {
        switch (cmd) {
            case START -> commandsActions.cmdStart(chatId);
            case HELP -> commandsActions.cmdHelp(chatId);
            case TASKS -> commandsActions.cmdTasks(chatId);
            default -> commandsActions.cmdUnknown(chatId);
        }
    }

    private Command parseCommand(String cmd) {
        try {
            return Command.valueOf(cmd.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Unknown command: " + cmd);
            return Command.UNKNOWN;
        }
    }
}
