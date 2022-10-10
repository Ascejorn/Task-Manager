package task;

import constants.Status;
import constants.TaskType;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask(TaskType task, String name, Status status, String description, int epicId) {
        super(task, name, status, description);
        this.epicId = epicId;
    }

    public Subtask(int id, TaskType type, String name, Status status, String description, int epicId) {
        super(id, type, name, status, description);
        this.epicId = epicId;
    }

    public Subtask(int id, TaskType type, String name, Status status, String description,
                   long duration, LocalDateTime startTime, int epicId) {
        super(id, type, name, status, description, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
