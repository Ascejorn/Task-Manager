package service;

import Exceptions.InvalidTimeException;
import Interfaces.HistoryManager;
import Interfaces.TaskManager;
import Utility.Managers;
import constants.Status;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Subtask> subTasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected int generatorId = 0;

    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void addTask(Task task) {
        validateTask(task);
        task.setId(generatorId++);
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void addEpic(Epic epic) {
        int epicId = generatorId++;
        epic.setId(epicId);
        epics.put(epicId, epic);
        prioritizedTasks.add(epic);
    }

    @Override
    public void addSubTask(Subtask subTask) {
        validateTask(subTask);
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        int subTaskId = generatorId++;
        subTask.setId(subTaskId);
        subTasks.put(subTaskId, subTask);
        prioritizedTasks.add(subTask);
        epic.addSubTask(subTaskId);
        updateEpicStatus(epic);
        updateEpicDurationAndStartTime(epicId);
    }

    private void updateEpicStatus(Epic epic) {
        List<Subtask> epicSubTasks = getEpicSubTasks(epic);
        int subTaskNewAmount = 0;
        int subTaskDoneAmount = 0;
        if (epicSubTasks == null) {
            epic.setStatus(Status.NEW);
        } else {
            for (Subtask subtask : epicSubTasks) {
                if (subtask.getStatus() == Status.NEW) {
                    subTaskNewAmount++;
                } else if (subtask.getStatus() == Status.DONE) {
                    subTaskDoneAmount++;
                }
            }
            if (subTaskNewAmount == epicSubTasks.size()) {
                epic.setStatus(Status.NEW);
            } else if (subTaskDoneAmount == epicSubTasks.size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    private void updateEpicDurationAndStartTime(Integer epicId) {
        Epic epic = epics.get(epicId);
        List<Integer> subtaskIds = epic.getSubTaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setDuration(0L);
            return;
        }
        LocalDateTime startEpic = LocalDateTime.MAX;
        LocalDateTime endEpic = LocalDateTime.MIN;
        long durationOfEpic = 0L;

        for (int id : subtaskIds) {
            Subtask subtask = subTasks.get(id);
            LocalDateTime startTime = subtask.getStartTime();
            LocalDateTime endTime = subtask.getEndTime();
            if (startTime.isBefore(startEpic)) {
                startEpic = startTime;
            }
            if (endTime.isAfter(endEpic)) {
                endEpic = endTime;
            }
            durationOfEpic += subtask.getDuration();
        }
        epic.setStartTime(startEpic);
        epic.setEndTime(endEpic);
        epic.setDuration(durationOfEpic);
    }

    private void validateTask(Task task) {
        LocalDateTime startTimeNewTask = task.getStartTime();
        long durationOfTask = task.getDuration();
        LocalDateTime endTimeNewTask = startTimeNewTask.plusMinutes(durationOfTask);
        Map<Integer, Task> myMapWithAllTasks = new HashMap<>();
        myMapWithAllTasks.putAll(tasks);
        myMapWithAllTasks.putAll(subTasks);

        Integer counter = myMapWithAllTasks.values().stream()
                .map(innerTask -> {
                    LocalDateTime startTimeOldTask = innerTask.getStartTime();
                    LocalDateTime endTimeOldTask = innerTask.getEndTime();
                    if (startTimeNewTask.isBefore(startTimeOldTask) && (endTimeNewTask.isBefore(startTimeOldTask)))
                        return 1;
                    if (startTimeNewTask.isAfter(endTimeOldTask) && (endTimeNewTask.isAfter(endTimeOldTask)))
                        return 1;
                    return 0;
                }).reduce(Integer::sum)
                .orElse(0);
        if (counter == myMapWithAllTasks.size()) {
            return;
        }
        if (myMapWithAllTasks.isEmpty()) {
            return;
        }
        throw new InvalidTimeException("Нельзя выполнять несколько задач одновременно - есть пересечение");
    }

    @Override
    public Task getTaskById(Integer taskId) {
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpicById(Integer epicId) {
        historyManager.add(epics.get(epicId));
        return epics.get(epicId);
    }

    @Override
    public Subtask getSubTaskById(Integer subTaskId) {
        historyManager.add(subTasks.get(subTaskId));
        return subTasks.get(subTaskId);
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public List<Subtask> getSubTasks() {
        return new ArrayList<>(this.subTasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(this.epics.values());
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic == null) {
            return;
        }
        epics.get(epic.getId()).setName(epic.getName());
        epics.get(epic.getId()).setDescription(epic.getDescription());
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        final int epicId = subtask.getEpicId();
        subTasks.put(subtask.getId(), subtask);
        updateEpicStatus(epics.get(epicId));
        updateEpicDurationAndStartTime(epicId);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void removeTasks() {
        tasks.clear();
    }

    @Override
    public void removeSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTaskIds().clear();
        }
    }

    @Override
    public void removeEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void removeTasksById(Integer id) {
        prioritizedTasks.remove(getTaskById(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubTasksById(Integer id) {
        prioritizedTasks.remove(getSubTaskById(id));
        Epic epic = epics.get(subTasks.get(id).getEpicId());
        epic.removeSubTask(id);
        subTasks.remove(id);
        updateEpicStatus(epic);
        updateEpicDurationAndStartTime(epic.getId());
        historyManager.remove(id);
    }

    @Override
    public void removeEpicById(Integer id) {
        if (epics.containsKey(id)) {
            Epic deleteEpic = epics.get(id);
            for (Subtask subTask : getEpicSubTasks(deleteEpic)) {
                prioritizedTasks.remove(subTask);
                removeSubTasksById(subTask.getId());
            }
            prioritizedTasks.remove(deleteEpic);
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public List<Subtask> getEpicSubTasks(Epic epic) {
        List<Subtask> subtaskList = new ArrayList<>();
        for (Integer subtaskId : epic.getSubTaskIds()) {
            subtaskList.add(subTasks.get(subtaskId));
        }
        return subtaskList;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
}
