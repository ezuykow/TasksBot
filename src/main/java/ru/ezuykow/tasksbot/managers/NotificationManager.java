package ru.ezuykow.tasksbot.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.ezuykow.tasksbot.entities.Task;
import ru.ezuykow.tasksbot.msgsender.MsgSender;
import ru.ezuykow.tasksbot.services.TaskService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class NotificationManager {

    private final Logger logger = LoggerFactory.getLogger(NotificationManager.class);

    private final TaskService taskService;
    private final MsgSender msgSender;

    public NotificationManager(TaskService taskService, MsgSender msgSender) {
        this.taskService = taskService;
        this.msgSender = msgSender;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void searchActualTasks() {
        logger.info("Searching actual tasks...");

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);

        List<Task> actualTasks = taskService.getActualTasks(date, time);
        msgSender.sendActualTasks(actualTasks);

        taskService.deleteTasks(actualTasks);
    }
}
