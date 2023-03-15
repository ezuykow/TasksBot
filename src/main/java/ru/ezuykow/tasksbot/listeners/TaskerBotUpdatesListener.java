package ru.ezuykow.tasksbot.listeners;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.ezuykow.tasksbot.commands.Commands;
import ru.ezuykow.tasksbot.services.TaskService;

import java.util.List;

@Service
public class TaskerBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TaskerBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final TaskService taskService;
    private final Commands commands;

    private final String fullTaskRegExp = "\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}\\.\\d{2} .+";
    private final String noTimeTaskRegExp = "\\d{2}\\.\\d{2}\\.\\d{4} .+";
    private final String noDateTaskRegExp = "\\d{2}[:\\.]\\d{2} .+";

    public TaskerBotUpdatesListener(TelegramBot telegramBot, TaskService taskService, Commands commands) {
        this.telegramBot = telegramBot;
        this.taskService = taskService;
        this.commands = commands;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            checkUpdateForCommand(update);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void checkUpdateForCommand(Update update) {
        String msg = update.message().text().trim();
        long chatId = update.message().chat().id();

        if (msg.charAt(0) == '/') {
            commands.doCommand(msg, chatId);
            return;
        }

        checkUpdateForNewTask(msg, chatId);
    }

    private void checkUpdateForNewTask(String msg, long chatId) {
        if (msg.matches(fullTaskRegExp)) {
            taskService.parseFullTask(msg, chatId);
        } else if (msg.matches(noDateTaskRegExp)){
            taskService.parseTaskWithoutDate(msg, chatId);
        } else if (msg.matches(noTimeTaskRegExp)) {
            taskService.parseTaskWithoutTime(msg, chatId);
        } else {
            logger.warn("This update is not task: {}", msg);
        }
    }
}
