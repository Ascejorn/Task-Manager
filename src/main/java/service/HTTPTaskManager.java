package service;

import Client.KVTaskClient;
import Utility.Managers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import task.Epic;
import task.Subtask;
import task.Task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTasksManager {

    public static final String TASKS_KEY = "tasks";
    public static final String EPICS_KEY = "epics";
    public static final String SUBTASKS_KEY = "subtasks";
    public static final String HISTORY_KEY = "history";
    private KVTaskClient kvTaskClient;
    private Gson gson;

    public HTTPTaskManager(int port) {
        super(null);
        gson = Managers.getGson();
        kvTaskClient = new KVTaskClient(port);
    }

    @Override
    protected void save() {
        String jsonTasks = gson.toJson(getTasks());
        kvTaskClient.put(TASKS_KEY, jsonTasks);

        String jsonEpics = gson.toJson(getEpics());
        kvTaskClient.put(EPICS_KEY, jsonEpics);

        String jsonSubtasks = gson.toJson(getSubTasks());
        kvTaskClient.put(SUBTASKS_KEY, jsonSubtasks);

        List<Integer> history = getHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList());

        String jsonHistory = gson.toJson(history);
        kvTaskClient.put(HISTORY_KEY, jsonHistory);
    }

    protected void load() {
        Type tasksType = new TypeToken<ArrayList<Task>>() {
        }.getType();

        List<Task> tasks = gson.fromJson(kvTaskClient.load(TASKS_KEY), tasksType);
        tasks.forEach(task -> {
            int id = task.getId();
            this.tasks.put(id, task);
            this.prioritizedTasks.add(task);
            if (id > generatorId) {
                generatorId = id;
            }
        });
        Type epicsType = new TypeToken<ArrayList<Epic>>() {
        }.getType();

        List<Epic> epics = gson.fromJson(kvTaskClient.load(EPICS_KEY), epicsType);
        epics.forEach(epic -> {
            int id = epic.getId();
            this.tasks.put(id, epic);
            this.prioritizedTasks.add(epic);
            if (id > generatorId) {
                generatorId = id;
            }
        });
        Type subtasksType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();

        List<Subtask> subtasks = gson.fromJson(kvTaskClient.load(SUBTASKS_KEY), subtasksType);
        subtasks.forEach(subtask -> {
            int id = subtask.getId();
            this.tasks.put(id, subtask);
            this.prioritizedTasks.add(subtask);
            if (id > generatorId) {
                generatorId = id;
            }
        });
        Type historyType = new TypeToken<ArrayList<Integer>>() {
        }.getType();

        List<Integer> history = gson.fromJson(kvTaskClient.load(HISTORY_KEY), historyType);
        for (Integer taskId : history) {
            if (this.tasks.containsKey(taskId)) {
                historyManager.add(getTaskById(taskId));
            } else if (this.epics.containsKey(taskId)) {
                historyManager.add(getEpicById(taskId));
            } else {
                historyManager.add(getSubTaskById(taskId));
            }
        }
    }
}
