package factory;

import containers.Container;
import containers.QueueContainer;
import containers.StackContainer;
import utils.Strategy;

public class TaskContainerFactory implements Factory {

    private static TaskContainerFactory instance=null;

    private TaskContainerFactory() {
    }

    public static TaskContainerFactory getInstance() {
        if(instance==null)
            instance=new TaskContainerFactory();
        return instance;
    }


    @Override
    public Container createContainer(Strategy strategy) {
        switch (strategy){
            case FIFO -> {
                return new QueueContainer();
            }
            case LIFO -> {
                return new StackContainer();
            }
            default -> {
                return null;
            }
        }
    }
}
