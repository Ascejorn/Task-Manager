package Utility;

import Interfaces.HistoryManager;
import constants.Status;
import constants.TaskType;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormat {

    private CSVTaskFormat() {
    }

    public static List<Integer> historyFromString(String value) {
        String[] strings = value.split(",");
        List<Integer> listOfTasks = new ArrayList<>();
        for (String stringNumber : strings) {
            listOfTasks.add(Integer.parseInt(stringNumber));
        }
        return listOfTasks;
    }

    public static Task fromString(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy; HH:mm");
        String[] values = value.split(",");
        if (TaskType.valueOf(values[1]) == TaskType.TASK) {
            return new Task(Integer.parseInt(values[0]), TaskType.valueOf(values[1]),
                    values[2], Status.valueOf(values[3]), values[4], Long.parseLong(values[6]),
                    LocalDateTime.parse(values[5], formatter));
        } else if (TaskType.valueOf(values[1]) == TaskType.EPIC) {
            return new Epic(Integer.parseInt(values[0]), TaskType.valueOf(values[1]),
                    values[2], Status.valueOf(values[3]), values[4], Long.parseLong(values[6]),
                    LocalDateTime.parse(values[5], formatter));
        } else {
            return new Subtask(Integer.parseInt(values[0]), TaskType.valueOf(values[1]),
                    values[2], Status.valueOf(values[3]), values[4], Integer.parseInt(values[6]),
                    LocalDateTime.parse(values[5], formatter), Integer.parseInt(values[8]));
        }
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        List<String> idList = new ArrayList<>();
        for (Task task : history) {
            int id = task.getId();
            idList.add(String.valueOf(id));
        }
        return String.join(",", idList);
    }

    public static String toString(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy; HH:mm");
        String startTimeFormatted = task.getStartTime().format(formatter);
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus()
                + "," + task.getDescription() + "," + startTimeFormatted + "," + task.getDuration()
                + "," + task.getEndTime();
    }
}
