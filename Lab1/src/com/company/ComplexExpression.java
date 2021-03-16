package com.company;
import utils.Operation;

public abstract class  ComplexExpression {
    private Operation operatie;
    private NumarComplex[] array;

    /**
     * Constructor
     * @param arr: NumarComplex[]
     */
    ComplexExpression(NumarComplex[] arr){
        this.setArray(arr);
    }

    /**
     * Metoda pentru evaluarea expresiei date
     * @return c: NumarComplex, rezultatul expresiei
     */
    public final NumarComplex execute(){
        NumarComplex c=this.array[0];
        for(int i=1;i<array.length;i++){
            c=executeOneOperation(c,array[i]);
        }
        return c;
    }

    /**
     * Metoda abstracta pentru executarea unei singure operatii
     * @param c1: NumarComplex
     * @param c2: NumarComplex
     * @return rez: NumarComplex, rez = c1 operatie c2
     */
    public abstract NumarComplex executeOneOperation(NumarComplex c1, NumarComplex c2);

    /**
     * Getter pentru operatia unei expresii
     * @return this.operatie: Operation
     */
    public final Operation getOperatie() {
        return operatie;
    }

    /**
     * Setter pentru operatia unei expresii
     * @param operatie: Operatie
     * post: this.operatie' = operatie
     */
    public final void setOperatie(Operation operatie) {
        this.operatie = operatie;
    }

    /**
     * Getter pentru vectorul de numere complexe care fac parte din expresie
     * @return array: NumarComplex[]
     */
    public final NumarComplex[] getArray() {
        return array;
    }

    /**
     * Setter pentru vectorul de numere complexe care fac parte din expresie
     * @param array: NumarComplex[]
     * post: this.array' = array
     */
    public final void setArray(NumarComplex[] array) {
        this.array = array;
    }


}
