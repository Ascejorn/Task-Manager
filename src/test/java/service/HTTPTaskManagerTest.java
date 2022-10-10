package service;

import Interfaces.TaskManagerTest;
import Server.KVServer;
import Utility.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {

    private KVServer kvServer;

    @BeforeEach
    void setUp() throws IOException {
        kvServer = Managers.getDefaultKVServer();
        kvServer.start();
        taskManager = new HTTPTaskManager(KVServer.PORT);
        taskManagerSetUp();
    }

    @AfterEach
    void tearDown() {
        kvServer.stop();
    }

    @Disabled
    @Test
    void save() {
    }

    @Test
    void load() {
        taskManager.getTaskById(task.getId());

        List<Task> tasks = taskManager.getTasks();
        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubTasks();

        assertNotNull(tasks, "Нет задач.");
        assertNotNull(epics, "Нет эпиков.");
        assertNotNull(subtasks, "Нет подзадач.");
        assertEquals(1, tasks.size(), "Не верное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        assertEquals(1, epics.size(), "Не верное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
        assertEquals(1, subtasks.size(), "Не верное количество подзадач.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");

        assertNotNull(taskManager.historyManager.getHistory(), "История просмотров отсутствует");
        assertEquals(taskManager.historyManager.getHistory().get(0), task, "Задачи в истории отличаются");
    }

    @Test
    void loadEmptyTask() {

        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.removeTasksById(task.getId());


        List<Task> tasks = taskManager.getTasks();
        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubTasks();

        assertTrue(tasks.isEmpty(), "Есть задачи.");
        assertNotNull(epics, "Нет эпиков.");
        assertNotNull(subtasks, "Нет подзадач.");
        assertEquals(1, epics.size(), "Не верное количество эпиков.");
        assertEquals(1, subtasks.size(), "Не верное количество подзадач.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");

        assertFalse(taskManager.historyManager.getHistory().isEmpty(), "Истории просмотра нет.");
        assertEquals(taskManager.historyManager.getHistory().get(0), epic, "Задачи в истории отличаются");
    }

    @Test
    void loadWithEpicWithoutSubtasks() {

        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubTaskById(subtask.getId());
        taskManager.removeSubTasksById(subtask.getId());

        List<Task> tasks = taskManager.getTasks();
        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubTasks();

        assertTrue(subtasks.isEmpty(), "Есть подзадачи.");
        assertNotNull(tasks, "Нет задач.");
        assertNotNull(epics, "Нет эпиков.");
        assertEquals(1, tasks.size(), "Не верное количество задач.");
        assertEquals(1, epics.size(), "Не верное количество эпиков.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");

        assertFalse(taskManager.historyManager.getHistory().isEmpty(), "Истории просмотра нет.");
        assertEquals(taskManager.historyManager.getHistory().get(0), task, "Задачи в истории отличаются");
        assertEquals(taskManager.historyManager.getHistory().get(1), epic, "Задачи в истории отличаются");
    }

    @Test
    void loadWithBlankHistory() {

        List<Task> tasks = taskManager.getTasks();
        List<Epic> epics = taskManager.getEpics();
        List<Subtask> subtasks = taskManager.getSubTasks();

        assertNotNull(tasks, "Нет задач.");
        assertNotNull(epics, "Нет эпиков.");
        assertNotNull(subtasks, "Нет подзадач.");
        assertEquals(1, tasks.size(), "Не верное количество задач.");
        assertEquals(1, epics.size(), "Не верное количество эпиков.");
        assertEquals(1, subtasks.size(), "Не верное количество подзадач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
        assertEquals(subtask, subtasks.get(0), "Подзадачи не совпадают.");

        assertTrue(taskManager.historyManager.getHistory().isEmpty(), "Истории просмотра нет.");
    }

}