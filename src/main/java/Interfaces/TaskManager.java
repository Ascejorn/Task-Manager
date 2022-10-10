package Interfaces;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.*;

public interface TaskManager {

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(Subtask subTask);

    Task getTaskById(Integer taskId);

    Epic getEpicById(Integer epicId);

    Subtask getSubTaskById(Integer subTaskId);

    List<Task> getTasks();

    List<Subtask> getSubTasks();

    List<Epic> getEpics();

    void updateEpic(Epic epic);

    void updateSubTask(Subtask subtask);

    void updateTask(Task task);

    void removeTasks();

    void removeSubTasks();

    void removeEpics();

    void removeTasksById(Integer id);

    void removeSubTasksById(Integer id);

    void removeEpicById(Integer id);

    List<Subtask> getEpicSubTasks(Epic epic);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

}
