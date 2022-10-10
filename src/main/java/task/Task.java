package task;

import constants.Status;
import constants.TaskType;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected int id;
    protected TaskType type;
    protected String name;
    protected Status status;
    protected String description;
    protected long duration;
    protected LocalDateTime startTime;

    public Task(TaskType type, String name, Status status, String description) {
        this.type = type;
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public Task(int id, TaskType type, String name, Status status, String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public Task(int id, TaskType type, String name, Status status, String description, long duration, LocalDateTime startTime) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.status = status;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        if (startTime == null) {
            return LocalDateTime.MAX;
        }
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return getStartTime().plusMinutes(duration);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
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
        Task task = (Task) o;
        return id == task.id && type == task.type && Objects.equals(name, task.name) &&
                status == task.status && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, status, description);
    }
}
