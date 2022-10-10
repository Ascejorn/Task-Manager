package task;

import Interfaces.TaskManager;
import constants.Status;
import constants.TaskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private TaskManager manager;

    private Epic epic;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        epic = new Epic(0, TaskType.EPIC, "task.Epic 1", Status.NEW, "описание 1-ого эпика");
        manager.addEpic(epic);
    }

    @Test
    void getEpicStatusWithEmptySubtasks() {
        assertNotNull(epic.getStatus(), "Статус отсутствует.");
        assertEquals(Status.NEW, epic.getStatus(), "Статус эпика не соответствует заданному изначально");
    }

    @Test
    void getEpicStatusWithSubtasksWithStatusNew() {
        Subtask subtask1 = new Subtask(1, TaskType.SUBTASK, "SubTask 1", Status.NEW,
                "описание 1", 10,
                LocalDateTime.of(2222, 1, 1, 1, 1), 0);
        Subtask subtask2 = new Subtask(2, TaskType.SUBTASK, "SubTask 2", Status.NEW,
                "описание 2", epic.getId());
        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);
        assertNotNull(epic.getSubTaskIds(), "Подзадачи отсутствуют");
        assertNotNull(epic.getStatus(), "Статус отсутствует.");
        assertEquals(Status.NEW, epic.getStatus(), "Статусы не освпадают.");
    }

    @Test
    void getEpicStatusWithSubtasksWithStatusDone() {
        Subtask subtask1 = new Subtask(1, TaskType.SUBTASK, "SubTask 1", Status.DONE,
                "описание 1", 10,
                LocalDateTime.of(2222, 1, 1, 1, 1), 0);
        Subtask subtask2 = new Subtask(2, TaskType.SUBTASK, "SubTask 2", Status.DONE,
                "описание 2", epic.getId());
        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);
        assertNotNull(epic.getSubTaskIds(), "Подзадачи отсутствуют");
        assertNotNull(epic.getStatus(), "Статус отсутствует.");
        assertEquals(Status.DONE, epic.getStatus(), "Статусы не освпадают.");
    }

    @Test
    void getEpicStatusWithSubtasksWithStatusDoneAndNew() {
        Subtask subtask1 = new Subtask(1, TaskType.SUBTASK, "SubTask 1", Status.DONE,
                "описание 1", 10,
                LocalDateTime.of(2222, 1, 1, 1, 1), 0);
        Subtask subtask2 = new Subtask(2, TaskType.SUBTASK, "SubTask 2", Status.NEW,
                "описание 2", epic.getId());
        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);
        assertNotNull(epic.getSubTaskIds(), "Подзадачи отсутствуют");
        assertNotNull(epic.getStatus(), "Статус отсутствует.");
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статусы не освпадают.");
    }

    @Test
    void getEpicStatusWithSubtasksWithStatusIn_Process() {
        Subtask subtask1 = new Subtask(1, TaskType.SUBTASK, "SubTask 1", Status.IN_PROGRESS,
                "описание 1", 10,
                LocalDateTime.of(2222, 1, 1, 1, 1), 0);
        Subtask subtask2 = new Subtask(2, TaskType.SUBTASK, "SubTask 2", Status.IN_PROGRESS,
                "описание 2", epic.getId());
        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);
        assertNotNull(epic.getSubTaskIds(), "Подзадачи отсутствуют");
        assertNotNull(epic.getStatus(), "Статус отсутствует.");
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статусы не освпадают.");
    }
}