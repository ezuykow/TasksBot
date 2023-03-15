package ru.ezuykow.tasksbot.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.ezuykow.tasksbot.entities.Task;
import ru.ezuykow.tasksbot.msgsender.MsgSender;
import ru.ezuykow.tasksbot.services.TaskService;

import java.util.List;

@Component
public class CommandsActions {

    private final Logger logger = LoggerFactory.getLogger(CommandsActions.class);

    @Value("${cmd.msg.start}")
    private String startMsg;
    @Value("${cmd.msg.help}")
    private String helpMsg;
    @Value("${cmd.msg.unknown}")
    private String unknownMsg;

    private final MsgSender msgSender;
    private final TaskService taskService;

    public CommandsActions(MsgSender msgSender, TaskService taskService) {
        this.msgSender = msgSender;
        this.taskService = taskService;
    }

    public void cmdStart(long chatId) {
        msgSender.sendCommandMsg(startMsg, chatId);
    }

    public void cmdHelp(long chatId) {
        msgSender.sendCommandMsg(helpMsg, chatId);
    }

    public void cmdUnknown(long chatId) {
        msgSender.sendCommandMsg(unknownMsg, chatId);
    }

    public void cmdTasks(long chatId) {
        List<Task> tasks = taskService.getAllTasks();
        msgSender.sendTasksList(tasks, chatId);
    }
}
