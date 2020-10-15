package com.company;
import domain.*;
import factory.SorterFactory;
import utils.SortingStrategy;

public class TestSortingTask {

    private static Integer[] createArray(){
        return new Integer[]{9,3,10,2,7,2,-2};
    }

    /*private static String[] createArray2(){
        return new String[]{"nimeni","ana","zet","karma"};
    }*/

    private static void testBubbleSort(){
        SortingTask task=new SortingTask("1","Sort",createArray(), SortingStrategy.BUBBLE_SORT);
        task.execute();
    }

    private static void testMergeSort(){
        SortingTask task=new SortingTask("1","Sort",createArray(),SortingStrategy.MERGE_SORT);
        task.execute();
    }

    public static void main(String[] args) {
        testBubbleSort();
        //testMergeSort();
    }
}
