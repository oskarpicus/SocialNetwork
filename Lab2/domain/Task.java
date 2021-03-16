package domain;

import java.util.Objects;

public abstract class Task {
    private String taskId;
    private String desc;

    public Task(String taskID, String desc) {
        this.taskId = taskID;
        this.desc = desc;
    }

    abstract public void execute();

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return taskId+" "+desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskId, task.taskId) &&
                Objects.equals(desc, task.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, desc);
    }
}
