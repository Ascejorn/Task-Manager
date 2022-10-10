package service;

import Interfaces.HistoryManager;
import constants.Status;
import constants.TaskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;

    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task = new Task(1, TaskType.TASK, "task.Task 1", Status.NEW, "описание таски 1");
        epic = new Epic(2, TaskType.EPIC, "task.Epic 1", Status.NEW, "описание 1-ого эпика");
        subtask = new Subtask(3, TaskType.SUBTASK, "SubTask 1", Status.NEW, "описание 1", epic.getId());
    }

    @Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void addTwice() {
        historyManager.add(task);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void getHistory() {
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Пустая история задач.");
        assertTrue(history.isEmpty(), "Пустая история задач.");
    }

    @Test
    void removeFirst() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(3, history.size(), "История не пустая.");

        historyManager.remove(task.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(2, history.size(), "История не пустая.");
        assertEquals(epic, history.get(0), "Порядок добавления.");
        assertEquals(subtask, history.get(1), "Порядок добавления.");
    }

    @Test
    void removeMid() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(3, history.size(), "История не пустая.");

        historyManager.remove(epic.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(2, history.size(), "История не пустая.");
        assertEquals(task, history.get(0), "Порядок добавления.");
        assertEquals(subtask, history.get(1), "Порядок добавления.");
    }

    @Test
    void removeLast() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(3, history.size(), "История не пустая.");

        historyManager.remove(subtask.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(2, history.size(), "История не пустая.");
        assertEquals(task, history.get(0), "Порядок добавления.");
        assertEquals(epic, history.get(1), "Порядок добавления.");
    }

    @Test
    void removeSingleTask() {
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");

        historyManager.remove(task.getId());
        history = historyManager.getHistory();
        assertNotNull(history, "Пустая история задач.");
        assertTrue(history.isEmpty(), "Пустая история задач.");
    }
}