package service;

import Interfaces.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private File file;

    @BeforeEach
    void setUp() {
        file = new File("src/test/resources/task.csv");
        taskManager = new FileBackedTasksManager(file);
        taskManagerSetUp();
    }

    @AfterEach
    void tearDown(){
        assertTrue(file.delete());
    }

    @Test
    void save() {
    }

    @Test
    void loadFromFile() {
        taskManager.getTaskById(task.getId());

        FileBackedTasksManager tasksManager = FileBackedTasksManager.loadFromFile(file);

        List<Task> tasks = tasksManager.getTasks();
        List<Epic> epics = tasksManager.getEpics();
        List<Subtask> subtasks = tasksManager.getSubTasks();

        assertNotNull(tasks, "Нет задач.");
        assertNotNull(epics, "Нет эпиков.");
        assertNotNull(subtasks, "Нет подзадач.");
        assertEquals(1, tasks.size(), "Не верное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        assertEquals(1, epics.size(), "Не верное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
        assertEquals(1, subtasks.size(), "Не верное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");

        assertNotNull(tasksManager.historyManager.getHistory(), "История просмотров отсутствует");
        assertEquals(tasksManager.historyManager.getHistory().get(0), task, "Задачи в истории отличаются");
    }

    @Test
    void loadFromEmptyTaskFile() {

        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.removeTasks();

        FileBackedTasksManager tasksManager = FileBackedTasksManager.loadFromFile(file);

        List<Task> tasks = tasksManager.getTasks();
        List<Epic> epics = tasksManager.getEpics();
        List<Subtask> subtasks = tasksManager.getSubTasks();

        assertTrue(tasks.isEmpty(), "Есть задачи.");
        assertNotNull(epics, "Нет эпиков.");
        assertNotNull(subtasks, "Нет подзадач.");
        assertEquals(1, epics.size(), "Не верное количество эпиков.");
        assertEquals(1, subtasks.size(), "Не верное количество подзадач.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");

        assertFalse(tasksManager.historyManager.getHistory().isEmpty(), "Истории просмотра нет.");
        assertEquals(tasksManager.historyManager.getHistory().get(0), epic, "Задачи в истории отличаются");
    }

    @Test
    void loadFromFileWithEpicWithoutSubtasks() {

        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubTaskById(subtask.getId());
        taskManager.removeSubTasks();

        FileBackedTasksManager tasksManager = FileBackedTasksManager.loadFromFile(file);

        List<Task> tasks = tasksManager.getTasks();
        List<Epic> epics = tasksManager.getEpics();
        List<Subtask> subtasks = tasksManager.getSubTasks();

        assertTrue(subtasks.isEmpty(), "Есть подзадачи.");
        assertNotNull(tasks, "Нет задач.");
        assertNotNull(epics, "Нет эпиков.");
        assertEquals(1, tasks.size(), "Не верное количество задач.");
        assertEquals(1, epics.size(), "Не верное количество эпиков.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");

        assertFalse(tasksManager.historyManager.getHistory().isEmpty(), "Истории просмотра нет.");
        assertEquals(tasksManager.historyManager.getHistory().get(0), task, "Задачи в истории отличаются");
        assertEquals(tasksManager.historyManager.getHistory().get(1), epic, "Задачи в истории отличаются");
    }

    @Test
    void loadFromFileWithBlankHistory() {
        FileBackedTasksManager tasksManager = FileBackedTasksManager.loadFromFile(file);

        List<Task> tasks = tasksManager.getTasks();
        List<Epic> epics = tasksManager.getEpics();
        List<Subtask> subtasks = tasksManager.getSubTasks();

        assertNotNull(tasks, "Нет задач.");
        assertNotNull(epics, "Нет эпиков.");
        assertNotNull(subtasks, "Нет подзадач.");
        assertEquals(1, tasks.size(), "Не верное количество задач.");
        assertEquals(1, epics.size(), "Не верное количество эпиков.");
        assertEquals(1, subtasks.size(), "Не верное количество подзадач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");

        assertTrue(tasksManager.historyManager.getHistory().isEmpty(), "Истории просмотра нет.");
    }

}