package containers;

import domain.Task;

public class QueueContainer extends AbstractContainer{

    @Override
    public Task remove() {
        //sterg primul element din vector
        if(isEmpty())
            return null;
        Task t=array[0];
        for(int i=1;i<array.length-1;i++)
            array[i-1]=array[i];
        size--;
        return t;
    }
}

/*public class QueueContainer implements Container {

    private Task[] array=new Task[10];
    private int size=0;

    @Override
    public Task remove() {
        //sterg primul element din vector
        if(isEmpty())
            return null;
        Task t=array[0];
        for(int i=1;i<array.length-1;i++)
            array[i-1]=array[i];
        size--;
        return t;
    }

    @Override
    public void add(Task task) {
        //adaug la finalul vectorului
        if(array.length==size)
            redimensionare();
        array[size++]=task;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    private void redimensionare(){
        Task[] nou=new Task[2*array.length];
        System.arraycopy(array, 0, nou, 0, array.length);
        array=nou;
    }
}*/
