package domain.sorters;

public class MergeSortSorter extends AbstractSorter{

    @Override
    public void sort(Comparable[] vector) {
        merge_sort(vector,0,vector.length-1,new Comparable[vector.length]);
    }

    private void merge_sort(Comparable[] vector,int st,int dr,Comparable[] aux){
        if(st<dr){
            int mij=(st+dr)/2;
            merge_sort(vector,st,mij,aux);
            merge_sort(vector,mij+1,dr,aux);
            merge(vector,st,mij,dr,aux);
        }
    }

    private void merge(Comparable[] x,int st,int mij,int dr,Comparable[] aux){
        int i=st,j=mij+1,k=st;
        while(i<=mij && j<=dr){
            if(x[i].compareTo(x[j])<0){
                aux[k]=x[i];
                i++;
            }
            else{
                aux[k]=x[j];
                j++;
            }
            k++;
        }
        while(i<=mij){
            aux[k]=x[i];
            k++;
            i++;
        }
        while(j<=dr){
            aux[k]=x[j];
            j++;
            k++;
        }
        for(int contor=st;contor<=dr;contor++)
            x[contor]=aux[contor];
    }
}
