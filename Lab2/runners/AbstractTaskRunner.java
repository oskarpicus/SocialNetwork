package runners;

import domain.Task;

public abstract class AbstractTaskRunner implements TaskRunner{ //Decorator

    private final TaskRunner taskRunner;

    public AbstractTaskRunner(TaskRunner taskRunner){
        this.taskRunner=taskRunner;
    }

    protected abstract void afterExecution();

    @Override
    public void executeOneTask() {
        taskRunner.executeOneTask();
        afterExecution();
    }

    @Override
    public void executeAll() {
        while(taskRunner.hasTask())
            this.executeOneTask();
    }

    @Override
    public void addTask(Task t) {
        taskRunner.addTask(t);
    }

    @Override
    public boolean hasTask() {
        return taskRunner.hasTask();
    }
}
