package factory;

import domain.sorters.AbstractSorter;
import domain.sorters.BubbleSortSorter;
import domain.sorters.MergeSortSorter;
import utils.SortingStrategy;

public class SorterFactory{
    private final static SorterFactory instance=new SorterFactory();

    private SorterFactory(){
    }

    public static SorterFactory getInstance(){
        return instance;
    }

    public AbstractSorter createSorter(SortingStrategy strategy){
        switch (strategy){
            case BUBBLE_SORT->{
                return new BubbleSortSorter();
            }
            case MERGE_SORT -> {
                return new MergeSortSorter();
            }
            default -> {
                return null;
            }
        }
    }
}
