package ru.ezuykow.tasksbot.msgsender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.ezuykow.tasksbot.entities.Task;
import ru.ezuykow.tasksbot.services.TaskService;

import java.util.List;

@Component
public class MsgSender {

    Logger logger = LoggerFactory.getLogger(MsgSender.class);

    private final TelegramBot bot;

    public MsgSender(TelegramBot bot) {
        this.bot = bot;
    }

    public void sendCommandMsg(String commandMsg, long chatId) {
        SendMessage msg = new SendMessage(chatId, commandMsg);
        SendResponse response = bot.execute(msg);
        checkResponse(response);
    }

    public void sendMessageAboutSaveTask(Task task) {
        SendMessage msg = new SendMessage(task.getChatId(),
                String.format("Задача \"%s\" успешно сохранена", task.getText()));
        SendResponse response = bot.execute(msg);
        checkResponse(response);
    }

    public void sendWrongDateTimeMsg(long chatId) {
        SendMessage msg = new SendMessage(chatId, "Некоректное дата/время!");
        SendResponse response = bot.execute(msg);
        checkResponse(response);
    }

    public void sendTasksList(List<Task> tasks, long chatId) {
        String text = makeAllTasksMsg(tasks);

        SendMessage msg = new SendMessage(chatId, text);
        SendResponse response = bot.execute(msg);
        checkResponse(response);
    }

    public void sendActualTasks(List<Task> tasks) {
        for (Task task : tasks) {
            String taskMsg = "  ТЕКУЩАЯ ЗАДАЧА:\n" + tasks;
            SendMessage msg = new SendMessage(task.getChatId(), taskMsg);
            SendResponse response = bot.execute(msg);
            checkResponse(response);
        }
    }

    private void checkResponse(SendResponse response) {
        if (!response.isOk()) {
            logger.warn("Message \"{}\" not sent", response.message());
        }
    }

    private String makeAllTasksMsg(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i+1).append(") ").append(tasks.get(i)).append("\n");
        }

        return sb.toString();
    }
}
