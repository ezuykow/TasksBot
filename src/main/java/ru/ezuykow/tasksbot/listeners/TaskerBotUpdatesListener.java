package ru.ezuykow.tasksbot.listeners;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ezuykow.tasksbot.commands.Commands;

import java.util.List;

@Service
public class TaskerBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TaskerBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    public TaskerBotUpdatesListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            doUpdate(update);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void doUpdate(Update update) {
        String msg = update.message().text().trim();
        checkForCommand(msg);
    }


}
