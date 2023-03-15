package ru.ezuykow.tasksbot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.ezuykow.tasksbot.entities.Task;
import ru.ezuykow.tasksbot.msgsender.MsgSender;
import ru.ezuykow.tasksbot.repositories.TasksRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TaskService {

    private final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TasksRepository tasksRepository;
    private final MsgSender msgSender;

    public TaskService(TasksRepository tasksRepository, MsgSender msgSender) {
        this.tasksRepository = tasksRepository;
        this.msgSender = msgSender;
    }

    public void parseFullTask(String msg, long chatId) {
        LocalDate date = parseDate(msg);
        LocalTime time = parseTime(msg, 11, 16);
        String text = msg.substring(17);

        createNewTask(date, time, text, chatId);
    }

    public void parseTaskWithoutDate(String msg, long chatId) {
        LocalDate date = LocalDate.now();
        LocalTime time = parseTime(msg, 0, 5);
        String text = msg.substring(6);

        createNewTask(date, time, text, chatId);
    }

    public void parseTaskWithoutTime(String msg, long chatId) {
        LocalDate date = parseDate(msg);
        LocalTime time = LocalTime.parse("10.00", DateTimeFormatter.ofPattern("HH.mm"));
        String text = msg.substring(11);

        createNewTask(date, time, text, chatId);
    }

    public List<Task> getAllTasks() {
        return tasksRepository.findAll();
    }

    public List<Task> getActualTasks(LocalDate date, LocalTime time) {
        return tasksRepository.getActualTask(date, time);
    }

    public void deleteTasks(List<Task> tasks) {
        for (Task task : tasks) {
            logger.info("Delete task {}", task.getId());
            tasksRepository.deleteById(task.getId());
        }
    }

    private boolean checkDateTimeNotBefore(LocalDate date, LocalTime time) {
        if (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            return false;
        }
        return !date.isBefore(LocalDate.now());
    }

    private LocalTime parseTime(String msg, int start, int end) {
        return LocalTime.parse(msg.substring(start, end), DateTimeFormatter.ofPattern("HH.mm"));
    }

    private LocalDate parseDate(String msg) {
        return LocalDate.parse(msg.substring(0, 10), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    private void createNewTask(LocalDate date, LocalTime time, String text, long chatId) {
        if (!checkDateTimeNotBefore(date, time)) {
            msgSender.sendWrongDateTimeMsg(chatId);
            return;
        }

        final Task task = new Task();
        task.setChatId(chatId);
        task.setDate(date);
        task.setText(text);
        task.setTime(time);

        tasksRepository.save(task);

        msgSender.sendMessageAboutSaveTask(task);
        logger.info("New task added {}", task);
    }
}
