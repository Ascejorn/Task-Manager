package service;

import Exceptions.ManagerSaveException;
import Utility.CSVTaskFormat;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        loadFromFile(new File("resources/tasks.csv"));
    }

    private void addNewTask(Task task) {
        final int id = task.getId();
        switch (task.getType()) {
            case TASK:
                tasks.put(id, task);
                break;
            case EPIC:
                epics.put(id, (Epic) task);
                break;
            case SUBTASK:
                subTasks.put(id, (Subtask) task);
                epics.get(((Subtask) task).getEpicId()).addSubTask(task.getId());
                break;
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(Subtask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public Task getTaskById(Integer taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(Integer epicId) {
        Epic epic = super.getEpicById(epicId);
        save();
        return epic;
    }

    @Override
    public Subtask getSubTaskById(Integer subTaskId) {
        Subtask subtask = super.getSubTaskById(subTaskId);
        save();
        return subtask;
    }

    @Override
    public List<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public List<Subtask> getSubTasks() {
        return super.getSubTasks();
    }

    @Override
    public List<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        super.updateSubTask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeSubTasks() {
        super.removeSubTasks();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void removeTasksById(Integer id) {
        super.removeTasksById(id);
        save();
    }

    @Override
    public void removeSubTasksById(Integer id) {
        super.removeSubTasksById(id);
        save();
    }

    @Override
    public void removeEpicById(Integer id) {
        super.removeEpicById(id);
        save();
    }

    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic,startDate,duration,endDate" + System.lineSeparator());
            for (Task task : tasks.values()) {
                writer.write(CSVTaskFormat.toString(task) + System.lineSeparator());
            }
            for (Epic epic : epics.values()) {
                writer.write(CSVTaskFormat.toString(epic) + System.lineSeparator());
            }
            for (Subtask subtask : subTasks.values()) {
                writer.write(CSVTaskFormat.toString(subtask) + "," + subtask.getEpicId() + System.lineSeparator());
            }
            writer.write(System.lineSeparator());
            writer.write(CSVTaskFormat.historyToString(historyManager));
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при записи файла!");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager tasksManager = new FileBackedTasksManager(file);
        List<Integer> history;
        try {
            String csv = Files.readString(file.toPath());
            String[] lines = csv.split(System.lineSeparator());
            int newTaskId = 0;
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (line.isEmpty()) {
                    history = CSVTaskFormat.historyFromString(lines[i + 1]);
                    for (Integer taskId : history) {
                        if (tasksManager.tasks.containsKey(taskId)) {
                            tasksManager.getTaskById(taskId);
                        } else if (tasksManager.epics.containsKey(taskId)) {
                            tasksManager.getEpicById(taskId);
                        } else {
                            tasksManager.getSubTaskById(taskId);
                        }
                    }
                    break;
                }
                Task task = CSVTaskFormat.fromString(line);
                int id = task.getId();
                if (id > newTaskId) {
                    newTaskId = id;
                }
                tasksManager.addNewTask(task);
            }
            tasksManager.generatorId = newTaskId;
            return tasksManager;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке файла!");
        }
    }
}




