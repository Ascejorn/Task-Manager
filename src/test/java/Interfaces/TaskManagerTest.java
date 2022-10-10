package Interfaces;

import constants.Status;
import constants.TaskType;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    protected void taskManagerSetUp() {
        task = new Task(0, TaskType.TASK, "Task 1", Status.NEW, "описание таски 1", 10,
                LocalDateTime.of(2222, 1, 1, 1, 1));
        taskManager.addTask(task);
        epic = new Epic(1, TaskType.EPIC, "Epic 1", Status.NEW, "описание 1-ого эпика");
        taskManager.addEpic(epic);
        subtask = new Subtask(2, TaskType.SUBTASK, "SubTask 1", Status.NEW, "описание 1", 10,
                LocalDateTime.of(2222, 1, 1, 2, 1), epic.getId());
        taskManager.addSubTask(subtask);
    }

    @Test
    void addNewTaskStandardVersion() {
        Task task1 = new Task(TaskType.TASK, "Test addNewTaskStandardVersion",
                Status.NEW, "Test addNewTaskStandardVersion description");
        taskManager.addTask(task1);
        final int taskId = task1.getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(1), "Задачи не совпадают.");
    }

    @Test
    void addNewTaskWithEmptyListOfTasks() {
        taskManager.removeTasks();
        assertTrue(taskManager.getTasks().isEmpty(), "Список задач не пуст");
        Task task1 = new Task(TaskType.TASK, "Test addNewTaskWithEmptyListOfTasks",
                Status.NEW, "Test addNewTaskWithEmptyListOfTasks description");
        taskManager.addTask(task1);
        final int taskId = task1.getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewTaskWithIncorrectId() {
        Task task2 = new Task(0, TaskType.TASK, "Test addNewTaskWithIncorrectId 2",
                Status.NEW, "Test addNewTaskWithIncorrectId description 2",
                10, LocalDateTime.of(2223, 1, 1, 1, 1));
        Task task3 = new Task(0, TaskType.TASK, "Test addNewTaskWithIncorrectId 3",
                Status.NEW, "Test addNewTaskWithIncorrectId description 3",
                20, LocalDateTime.of(2224, 1, 2, 1, 1));
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        final int task2Id = task2.getId();
        final int task3Id = task3.getId();

        final Task savedTask2 = taskManager.getTaskById(task2Id);
        final Task savedTask3 = taskManager.getTaskById(task3Id);

        assertNotNull(savedTask2, "Задача не найдена.");
        assertNotNull(savedTask3, "Задача не найдена.");
        assertEquals(task2, savedTask2, "Задачи не совпадают.");
        assertEquals(task3, savedTask3, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(task2Id, 3, "ID2 не совпадает.");
        assertEquals(task3Id, 4, "ID3 не совпадает.");
        assertEquals(task2, tasks.get(1), "Задача 2 не совпадает.");
        assertEquals(task3, tasks.get(2), "Задача 3 не совпадает.");
    }

    @Test
    void addNewEpicStandardVersion() {
        Epic epic1 = new Epic(TaskType.EPIC, "Test addNewEpicStandardVersion 1", Status.NEW,
                "Test addNewEpicStandardVersion description 1");
        taskManager.addEpic(epic1);
        final int epic1Id = epic1.getId();

        final Epic savedTask = taskManager.getEpicById(epic1Id);

        assertNotNull(savedTask, "Эпик не найден.");
        assertEquals(epic1, savedTask, "Эпик не совпадает.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic1, epics.get(1), "Эпик 1 не совпадает.");
        assertNotNull(epic1.getStatus(), "Статус Эпика 1 отсутствует.");
        assertEquals(Status.NEW, epic1.getStatus(), "Статусы эпика не совпадают.");
    }

    @Test
    void addNewEpicWithEmptyListOfEpics() {
        taskManager.removeEpics();
        assertTrue(taskManager.getEpics().isEmpty(), "Список эпиков не пуст");
        Epic epic1 = new Epic(TaskType.EPIC, "Test addNewEpicWithEmptyListOfEpics 1", Status.NEW,
                "Test addNewEpicWithEmptyListOfEpics description 1");
        taskManager.addEpic(epic1);
        final int epic1Id = epic1.getId();

        final Epic savedTask = taskManager.getEpicById(epic1Id);

        assertNotNull(savedTask, "Эпик не найден.");
        assertEquals(epic1, savedTask, "Эпик не совпадает.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic1, epics.get(0), "Эпик 1 не совпадает.");
        assertNotNull(epic1.getStatus(), "Статус Эпика 1 отсутствует.");
        assertEquals(Status.NEW, epic1.getStatus(), "Статусы эпика не совпадают.");
    }

    @Test
    void addNewEpicWithIncorrectId() {
        Epic epic2 = new Epic(0, TaskType.EPIC, "Test addNewEpicWithIncorrectId 2", Status.NEW,
                "Test addNewEpicWithIncorrectId description 2");
        Epic epic3 = new Epic(0, TaskType.EPIC, "Test addNewEpicWithIncorrectId 3", Status.NEW,
                "Test addNewEpicWithIncorrectId description 3");
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);
        final int epic2Id = epic2.getId();
        final int epic3Id = epic3.getId();

        final Epic savedEpic2 = taskManager.getEpicById(epic2Id);
        final Epic savedEpic3 = taskManager.getEpicById(epic3Id);

        assertNotNull(savedEpic2, "Эпик 2 не найден.");
        assertNotNull(savedEpic3, "Эпик 3 не найден.");
        assertEquals(epic2, savedEpic2, "Эпики 2 не совпадают.");
        assertEquals(epic3, savedEpic3, "Эпики 3 не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(3, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic2Id, 3, "ID2 не совпадает.");
        assertEquals(epic3Id, 4, "ID3 не совпадает.");
        assertEquals(epic2, epics.get(1), "Эпик 2 не совпадает.");
        assertEquals(epic3, epics.get(2), "Эпик 3 не совпадает.");
        assertNotNull(epic2.getStatus(), "Статус Эпика 2 отсутствует.");
        assertNotNull(epic3.getStatus(), "Статус Эпика 2 отсутствует.");
        assertEquals(Status.NEW, epic2.getStatus(), "Статусы эпика не совпадают.");
        assertEquals(Status.NEW, epic3.getStatus(), "Статусы эпика не совпадают.");
    }

    @Test
    void addNewSubTaskStandardVersion() {
        Subtask subtask1 = new Subtask(3, TaskType.SUBTASK, "SubTask 1", Status.NEW,
                "описание 1", 10,
                LocalDateTime.of(2232, 1, 1, 1, 1), 1);
        assertNotNull(taskManager.getEpicById(1), "Эпик подзадачи отсутствует.");
        taskManager.addSubTask(subtask1);
        final int subtask1Id = subtask1.getId();

        final Subtask savedSubTask = taskManager.getSubTaskById(subtask1Id);

        assertNotNull(savedSubTask, "Подзадача не найдена.");
        assertEquals(subtask1, savedSubTask, "Подзадача не совпадает.");

        final List<Subtask> subtasks = taskManager.getSubTasks();
        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask1, subtasks.get(1), "Ползадача 1 не совпадает.");
        assertNotNull(taskManager.getEpicById(subtask1.getEpicId()).getStatus(),
                "Статус Эпика у подхадачи отсутствует.");
        assertEquals(Status.NEW, taskManager.getEpicById(subtask1.getEpicId()).getStatus(),
                "Статусы эпика не совпадают.");
    }

    @Test
    void addNewSubTaskWithEmptyListOfSubtasks() {
        taskManager.removeSubTasks();
        assertTrue(taskManager.getSubTasks().isEmpty(), "Список подзадач не пуст");
        Subtask subtask1 = new Subtask(2, TaskType.SUBTASK, "SubTask 1", Status.NEW,
                "описание 1", 10,
                LocalDateTime.of(2232, 1, 1, 1, 1), 1);
        assertNotNull(taskManager.getEpicById(1), "Эпик подзадачи отсутствует.");
        taskManager.addSubTask(subtask1);
        final int subtask1Id = subtask1.getId();

        final Subtask savedSubTask = taskManager.getSubTaskById(subtask1Id);

        assertNotNull(savedSubTask, "Подзадача не найдена.");
        assertEquals(subtask1, savedSubTask, "Подзадача не совпадает.");

        final List<Subtask> subtasks = taskManager.getSubTasks();
        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask1, subtasks.get(0), "Ползадача 1 не совпадает.");
        assertNotNull(taskManager.getEpicById(subtask1.getEpicId()).getStatus(),
                "Статус Эпика у подхадачи отсутствует.");
        assertEquals(Status.NEW, taskManager.getEpicById(subtask1.getEpicId()).getStatus(),
                "Статусы эпика не совпадают.");
    }

    @Test
    void addNewSubtaskWithIncorrectId() {
        Subtask subtask2 = new Subtask(0, TaskType.SUBTASK, "SubTask 1", Status.NEW,
                "описание 1", 10,
                LocalDateTime.of(2232, 1, 1, 1, 1), 1);
        Subtask subtask3 = new Subtask(9, TaskType.SUBTASK, "SubTask 1", Status.NEW,
                "описание 1", 10,
                LocalDateTime.of(2242, 1, 1, 1, 1), 1);
        assertNotNull(taskManager.getEpicById(1), "Эпик подзадачи отсутствует.");
        taskManager.addSubTask(subtask2);
        taskManager.addSubTask(subtask3);
        final int subtask2Id = subtask2.getId();
        final int subtask3Id = subtask3.getId();

        final Subtask savedSubTask2 = taskManager.getSubTaskById(subtask2Id);
        final Subtask savedSubTask3 = taskManager.getSubTaskById(subtask3Id);

        assertNotNull(savedSubTask2, "Подзадача 2 не найдена.");
        assertNotNull(savedSubTask3, "Подзадача 3 не найдена.");
        assertEquals(subtask2, savedSubTask2, "Подзадачи 2 не совпадают.");
        assertEquals(subtask3, savedSubTask3, "Подзадачи 3 не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubTasks();

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество эпиков.");
        assertEquals(subtask2Id, 3, "ID2 не совпадает.");
        assertEquals(subtask3Id, 4, "ID3 не совпадает.");
        assertEquals(subtask2, subtasks.get(1), "Подзадача 2 не совпадает.");
        assertEquals(subtask3, subtasks.get(2), "Подзадача 3 не совпадает.");
        assertNotNull(taskManager.getEpicById(subtask2.getEpicId()).getStatus(),
                "Статус Эпика у подхадачи отсутствует.");
        assertEquals(Status.NEW, taskManager.getEpicById(subtask2.getEpicId()).getStatus(),
                "Статусы эпика не совпадают.");
        assertNotNull(taskManager.getEpicById(subtask3.getEpicId()).getStatus(),
                "Статус Эпика у подхадачи отсутствует.");
        assertEquals(Status.NEW, taskManager.getEpicById(subtask3.getEpicId()).getStatus(),
                "Статусы эпика не совпадают.");
    }

    @Test
    void addNewSubtaskWithIncorrectEpicId() {
        Subtask subtask1 = new Subtask(3, TaskType.SUBTASK, "SubTask 1", Status.NEW,
                "описание 1", 10,
                LocalDateTime.of(2232, 1, 1, 1, 1), 10);
        assertNull(taskManager.getEpicById(10), "Эпик подзадачи не отсутствует.");
        taskManager.addSubTask(subtask1);
        final int subtask1Id = subtask1.getId();

        final Subtask savedSubTask1 = taskManager.getSubTaskById(subtask1Id);

        assertNull(savedSubTask1, "Подзадача 1 найдена.");

        final List<Subtask> subtasks = taskManager.getSubTasks();

        assertNotNull(subtasks, "Подзадачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество Подзадач.");
    }

    @Test
    void getTaskByIdStandardVersion() {
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void getTaskByIdWithEmptyTaskList() {
        taskManager.removeTasks();
        assertTrue(taskManager.getTasks().isEmpty(), "Задачи найдены");
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNull(savedTask, "Задача найдена.");
    }

    @Test
    void getTaskByIdWithIncorrectTaskId() {
        final Task savedTask = taskManager.getTaskById(6);

        assertNull(savedTask, "Задача найдена.");
    }

    @Test
    void getEpicByIdStandardVersion() {
        final int epicId = epic.getId();

        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");
    }

    @Test
    void getEpicByIdWithEmptyEpicList() {
        taskManager.removeEpics();
        assertTrue(taskManager.getEpics().isEmpty(), "Эпики найдены");
        final int epicId = epic.getId();

        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNull(savedEpic, "Эпик найден.");
    }

    @Test
    void getEpicByIdWithIncorrectEpicId() {
        final Epic savedEpic = taskManager.getEpicById(10);

        assertNull(savedEpic, "Эпик найдена.");
    }

    @Test
    void getSubTasksByIdStandardVersion() {
        final int subtaskId = subtask.getId();

        final Subtask savedSubtask = taskManager.getSubTaskById(subtaskId);

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");
    }

    @Test
    void getSubTasksByIdWithEmptySubTasksList() {
        taskManager.removeSubTasks();
        assertTrue(taskManager.getSubTasks().isEmpty(), "Подзадачи найдены");
        final int subTaskId = subtask.getId();

        final Subtask savedSubTask = taskManager.getSubTaskById(subTaskId);

        assertNull(savedSubTask, "Подзадача найдена.");
    }

    @Test
    void getSubTasksByIdWithIncorrectSubTasksId() {
        final Subtask savedSubTask = taskManager.getSubTaskById(10);

        assertNull(savedSubTask, "Подзадача найдена.");
    }

    @Test
    void getTasksStandardVersion() {
        List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Получение задач не выполняется.");
        assertEquals(1, tasks.size(), "Количество задач неправильное.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getTasksWithEmptyTasksList() {
        taskManager.removeTasks();
        List<Task> tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty(), "Получение задач выполняется.");
    }

    @Test
    void getSubTasksStandardVersion() {
        Subtask subtask1 = new Subtask(3, TaskType.SUBTASK, "SubTask 1", Status.NEW,
                "описание 1", 10,
                LocalDateTime.of(2232, 1, 1, 1, 1), 1);
        taskManager.addSubTask(subtask1);
        assertEquals(2, taskManager.getSubTasks().size(), "Количество подзадач не совпадает.");

        final List<Subtask> savedSubtaskList = taskManager.getSubTasks();
        assertNotNull(savedSubtaskList, "Подзадачи не найдены.");
        assertEquals(savedSubtaskList.get(0), taskManager.getSubTaskById(2),
                "Подзадача 0 не совпадает.");
        assertEquals(savedSubtaskList.get(1), taskManager.getSubTaskById(3),
                "Подзадача 1 не совпадает.");
    }

    @Test
    void getSubTasksWithEmptySubtaskList() {
        Subtask subtask1 = new Subtask(3, TaskType.SUBTASK, "SubTask 1", Status.NEW,
                "описание 1", 10,
                LocalDateTime.of(2232, 1, 1, 1, 1), 1);
        taskManager.addSubTask(subtask1);
        taskManager.removeSubTasks();
        assertTrue(taskManager.getSubTasks().isEmpty(), "Количество подзадач не совпадает.");

        final List<Subtask> savedSubtaskList = taskManager.getSubTasks();
        assertEquals(savedSubtaskList, taskManager.getSubTasks(),
                "Подзадачи не совпадают.");
        assertTrue(savedSubtaskList.isEmpty(), "Количество подзадач не совпадает.");
    }

    @Test
    void getEpicsStandardVersion() {
        Epic epic2 = new Epic(0, TaskType.EPIC, "Test getEpicsStandardVersion 2", Status.NEW,
                "Test getEpicsStandardVersion description 2");
        taskManager.addEpic(epic2);
        assertEquals(2, taskManager.getEpics().size(), "Количество эпиков не совпадает.");

        final List<Epic> savedEpicList = taskManager.getEpics();
        assertNotNull(savedEpicList, "Эпики не найдены.");
        assertEquals(savedEpicList.get(0), taskManager.getEpicById(1),
                "Подзадача 0 не совпадает.");
        assertEquals(savedEpicList.get(1), taskManager.getEpicById(3),
                "Подзадача 1 не совпадает.");
    }

    @Test
    void getEpicsWithEmptyEpicList() {
        Epic epic2 = new Epic(0, TaskType.EPIC, "Test getEpicsWithEmptyEpicList 2", Status.NEW,
                "Test getEpicsWithEmptyEpicList description 2");
        taskManager.addEpic(epic2);
        taskManager.removeEpics();
        assertTrue(taskManager.getEpics().isEmpty(), "Количество Эпиков не совпадает.");

        final List<Epic> savedEpicList = taskManager.getEpics();
        assertEquals(savedEpicList, taskManager.getEpics(),
                "Эпики не совпадают.");
        assertTrue(savedEpicList.isEmpty(), "Количество Эпиков не совпадает.");
    }

    @Test
    void updateEpicStandardVersion() {
        epic = new Epic(1, TaskType.EPIC, "Новое имя", Status.NEW, "Новое описание");
        taskManager.updateEpic(epic);
        assertEquals(epic.getName(), "Новое имя", "Название эпика не совпадает с новым.");
        assertEquals(epic.getDescription(), "Новое описание", "Описание эпика не совпадает с новым.");
    }

    @Test
    void updateSubTaskStandardVersion() {
        subtask = new Subtask(2, TaskType.SUBTASK, "Новое имя подзадачи",
                Status.NEW, "Новое описание ползадачи", 10,
                LocalDateTime.of(2222, 1, 1, 2, 1), epic.getId());
        taskManager.updateSubTask(subtask);
        assertEquals(taskManager.getSubTaskById(2), subtask, "Подзадача не поменялась");
        assertEquals(taskManager.getSubTasks().get(0).getName(), "Новое имя подзадачи",
                "Название задачи не совпадает");
        assertEquals(taskManager.getSubTasks().get(0).getDescription(), "Новое описание ползадачи",
                "Описание задачи не совпадает");
    }

    @Test
    void updateTaskStandardVersion() {
        task = new Task(0, TaskType.TASK, "Новое имя", Status.NEW, "Новое описание",
                10, LocalDateTime.of(2222, 1, 1, 1, 1));
        taskManager.updateTask(task);
        assertEquals(taskManager.getTaskById(0), task, "Подзадача не поменялась");
        assertEquals(taskManager.getTasks().get(0).getName(), "Новое имя",
                "Название задачи не совпадает");
        assertEquals(taskManager.getTasks().get(0).getDescription(), "Новое описание",
                "Описание задачи не совпадает");
    }

    @Test
    void removeTasks() {
        taskManager.removeTasks();
        assertTrue(taskManager.getTasks().isEmpty(), "Список задач не пуст.");
    }

    @Test
    void removeSubTasks() {
        taskManager.removeSubTasks();
        assertTrue(taskManager.getSubTasks().isEmpty(), "Список подзадач не пуст.");
        assertTrue(taskManager.getEpicById(1).getSubTaskIds().isEmpty(), "Список подзадач у эпика не пуст.");
    }

    @Test
    void removeEpics() {
        taskManager.removeEpics();
        assertTrue(taskManager.getEpics().isEmpty(), "Список эпиков не пуст.");
        assertTrue(taskManager.getSubTasks().isEmpty(), "Список подзадач эпика не пуст.");
    }

    @Test
    void removeTasksById() {
        taskManager.getTaskById(0);
        taskManager.removeTasksById(0);
        assertEquals(taskManager.getPrioritizedTasks().size(),2, "Количество задач отличается");
        assertEquals(taskManager.getPrioritizedTasks().get(0),subtask, "Задачи списка приоритета отличается");
        assertEquals(taskManager.getPrioritizedTasks().get(1),epic, "Задачи списка приоритета отличается");
        assertNull(taskManager.getTaskById(0), "Задача не удалена.");
        assertTrue(taskManager.getHistory().isEmpty(), "Список истории не пуст.");
    }

    @Test
    void removeSubTasksById() {
        taskManager.getSubTaskById(2);
        taskManager.removeSubTasksById(2);
        assertEquals(taskManager.getPrioritizedTasks().size(),2, "Количество задач отличается");
        assertEquals(taskManager.getPrioritizedTasks().get(0),task, "Задачи списка приоритета отличается");
        assertEquals(taskManager.getPrioritizedTasks().get(1),epic, "Задачи списка приоритета отличается");
        assertNull(taskManager.getSubTaskById(2), "Подзадача не удалена.");
        assertTrue(taskManager.getSubTasks().isEmpty(), "Подзадача не удалена.");
        assertTrue(taskManager.getEpics().get(0).getSubTaskIds().isEmpty(), "Подзадача у эпика не удалена.");
        assertTrue(taskManager.getHistory().isEmpty(), "Список истории не пуст.");

    }

    @Test
    void removeEpicById() {
        taskManager.getEpicById(1);
        taskManager.removeEpicById(1);
        assertEquals(taskManager.getPrioritizedTasks().size(),1, "Количество задач отличается");
        assertEquals(taskManager.getPrioritizedTasks().get(0),task, "Задачи списка приоритета отличается");
        assertTrue(taskManager.getEpics().isEmpty(), "Эпик не удален.");
        assertTrue(taskManager.getSubTasks().isEmpty(), "Подзадача эпика не удалена.");
        assertTrue(taskManager.getHistory().isEmpty(), "Список истории не пуст.");
    }

    @Test
    void getEpicSubTasks() {
        final int subTaskId = taskManager.getEpicSubTasks(epic).get(0).getId();

        final Subtask savedSubTask = taskManager.getSubTaskById(subTaskId);

        assertNotNull(savedSubTask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubTask, "Подзадачи не совпадают.");
    }

    @Test
    void getHistory() {
        taskManager.getTaskById(0);
        assertEquals(taskManager.getHistory().get(0), task, "Список истории пуст.");
    }

    @Test
    void getPrioritizedTasks() {
        assertEquals(taskManager.getPrioritizedTasks().size(), 3,
                "Количество задач в списке приоритета не совпадает");
        assertEquals(taskManager.getPrioritizedTasks().get(0), task, "Первая в списке на задача.");
    }
}