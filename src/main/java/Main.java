import Interfaces.TaskManager;
import Server.KVServer;
import Utility.Managers;
import constants.Status;
import constants.TaskType;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {

        KVServer kvServer = Managers.getDefaultKVServer();
        kvServer.start();
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task(TaskType.TASK, "task.Task 1", Status.NEW, "описание таски 1");
        Task task2 = new Task(0,TaskType.TASK, "task.Task 2", Status.NEW, "описание таски 2",10, LocalDateTime.of(2222, 1, 1, 1, 1));
        Task task3 = new Task(0,TaskType.TASK, "task.Task 3", Status.NEW, "описание таски 3",20,LocalDateTime.of(2222, 1, 2, 1, 1));


        manager.addTask(task2);
        manager.addTask(task3);

        System.out.println(manager.getPrioritizedTasks());
        manager.removeTasksById(1);
        System.out.println(manager.getPrioritizedTasks());

        Epic epic1 = new Epic(TaskType.EPIC, "task.Epic 1", Status.NEW, "описание 1-ого эпика");
        Epic epic2 = new Epic(TaskType.EPIC, "task.Epic 2", Status.NEW, "описание 2-ого эпика");
        Epic epic3= new Epic(4,TaskType.EPIC, "task.Epic 3", Status.NEW, "описание 3-ого эпика");

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);

        Subtask subTask1 = new Subtask(3,TaskType.SUBTASK, "SubTask 1", Status.NEW,
                "описание 1", 110,
                LocalDateTime.of(2272, 1, 1, 1, 1),3);
        Subtask subTask2 = new Subtask(4,TaskType.SUBTASK, "SubTask 2", Status.NEW,
                "описание 2", 120,
                LocalDateTime.of(2232, 1, 1, 1, 1),3);
        Subtask subTask3 = new Subtask(5,TaskType.SUBTASK, "SubTask 3", Status.NEW,
                "описание 3", 130,
                LocalDateTime.of(2242, 1, 1, 1, 1),3);
//
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);
        manager.getSubTaskById(6);
        manager.getHistory();

        kvServer.stop();
//
//
////
//        manager.getTaskById(0);
//        manager.getTaskById(1);
//        subTask1.setEpicId(4);
//        manager.removeTasksById(0);
//        manager.removeTasksById(0);
//        manager.removeTasksById(0);
//        manager.removeTasksById(0);

//        manager.getTaskById(1);
//        manager.getEpicById(2);
//        manager.getEpicById(3);
//        manager.getSubTaskById(4);
//        manager.getSubTaskById(5);
//        System.out.println(manager.getHistory());
//
//        manager.removeTasksById(1);
//        System.out.println(manager.getHistory());
//
//        manager.removeEpicById(2);
//        System.out.println(manager.getHistory());
//        Epic epic1 = new Epic(0,TaskType.EPIC, "task.Epic 1", Status.NEW, "описание 1-ого эпика");
//        Subtask subTask1 = new Subtask(1,TaskType.SUBTASK, "SubTask 1", Status.NEW, "описание 1", 60, LocalDateTime.now(), epic1.getId());
//        Subtask subTask2 = new Subtask(2,TaskType.SUBTASK, "SubTask 2", Status.NEW, "описание 2", epic1.getId());
//        manager.addEpic(epic1);
//        manager.addSubTask(subTask1);
//        manager.addSubTask(subTask2);
//        System.out.println(subTask1.getDuration());
//        System.out.println(epic1.getDuration());
//        System.out.println(manager.getPrioritizedTasks());
    }
}