package task;

import constants.Status;
import constants.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private final List<Integer> subTaskIds;
    private LocalDateTime endTime;

    public Epic(TaskType type, String name, Status status, String description) {
        super(type, name, status, description);
        this.subTaskIds = new ArrayList<>();
    }

    public Epic(int id, TaskType type, String name, Status status, String description) {
        super(id, type, name, status, description);
        this.subTaskIds = new ArrayList<>();
    }

    public Epic(int id, TaskType type, String name, Status status, String description,
                long duration, LocalDateTime startTime) {
        super(id, type, name, status, description, duration, startTime);
        this.subTaskIds = new ArrayList<>();
    }

    public Epic(int id, TaskType type, String name, Status status, String description,
                long duration, LocalDateTime startTime, List<Integer> subTaskIds) {
        super(id, type, name, status, description, duration, startTime);
        this.subTaskIds = subTaskIds;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void addSubTask(int subTaskId) {
        subTaskIds.add(subTaskId);
    }

    public void removeSubTask(Integer subTaskId) {
        subTaskIds.remove(subTaskId);
    }

    public List<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTaskIds=" + subTaskIds +
                ", endTime=" + endTime +
                ", id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTaskIds, epic.subTaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTaskIds);
    }
}
