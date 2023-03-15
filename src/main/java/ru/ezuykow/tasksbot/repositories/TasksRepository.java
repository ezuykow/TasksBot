package ru.ezuykow.tasksbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.ezuykow.tasksbot.entities.Task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TasksRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT * FROM tasks WHERE date=:date AND time=:time", nativeQuery = true)
    List<Task> getActualTask(LocalDate date, LocalTime time);
}
