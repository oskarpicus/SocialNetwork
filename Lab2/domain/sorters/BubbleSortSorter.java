package domain.sorters;

public class BubbleSortSorter extends AbstractSorter {

    @Override
    public void sort(Comparable[] vector){
        boolean ok;
        do{
            ok=false;
            for(int i=0;i<vector.length-1;i++)
                if(vector[i].compareTo(vector[i+1])>0){
                    Comparable aux;
                    aux=vector[i];
                    vector[i]=vector[i+1];
                    vector[i+1]=aux;
                    ok=true;
                }
        }while(ok);
    }

}
