package factory;
import containers.Container;
import utils.Strategy;

public interface Factory {
    Container createContainer(Strategy strategy);
}
