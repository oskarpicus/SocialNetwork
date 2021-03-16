package containers;

import domain.Task;

public class StackContainer extends AbstractContainer{

    @Override
    public Task remove() {
        if(isEmpty())
            return null;
        return array[--size];
    }
}

/*public class StackContainer implements Container {

    private Task[] array=new Task[10];
    private int size=0;

    @Override
    public Task remove() { //elimin ultimul adaugat
        if(isEmpty())
            return null;
        return array[--size];
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
