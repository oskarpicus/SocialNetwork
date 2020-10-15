package runners;

import containers.Container;
import domain.Task;
import factory.TaskContainerFactory;
import utils.Strategy;

public class StrategyTaskRunner implements TaskRunner{

    private final Container container;

    public StrategyTaskRunner(Strategy strategy){
        container= TaskContainerFactory.getInstance().createContainer(strategy);
    }

    @Override
    public void executeOneTask() {
        if(!container.isEmpty())
            container.remove().execute();
    }

    @Override
    public void executeAll() {
        while(!container.isEmpty())
            container.remove().execute();
    }

    @Override
    public void addTask(Task t) {
        container.add(t);
    }

    @Override
    public boolean hasTask() {
        return !container.isEmpty();
    }
}
