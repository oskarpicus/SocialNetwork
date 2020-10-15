package containers;

import domain.Task;

public abstract class AbstractContainer implements Container{

    protected int size=0;
    protected Task[] array=new Task[10];

    public abstract Task remove();

    public void add(Task task){
        //adaug la finalul vectorului
        if(array.length==size)
            redimensionare();
        array[size++]=task;
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size==0;
    }

    private void redimensionare(){
        Task[] nou=new Task[2*array.length];
        System.arraycopy(array, 0, nou, 0, array.length);
        array=nou;
    }

}
