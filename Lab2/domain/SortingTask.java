package domain;
import domain.sorters.AbstractSorter;
import utils.SortingStrategy;
import factory.SorterFactory;

public class SortingTask extends Task {
    private final Comparable[] vector; //vectorul de sortat
    private final AbstractSorter sorter; //strategia de sortare

    public SortingTask(String taskID, String descriere, Comparable[] vector,SortingStrategy strategy) {
        super(taskID, descriere);
        this.vector = vector;
        sorter=SorterFactory.getInstance().createSorter(strategy);
    }

    @Override
    public void execute() {
        sorter.sort(vector);
        printArray();
    }

    private void printArray(){
        for(Comparable e : vector)
            System.out.println(e);
    }
}
