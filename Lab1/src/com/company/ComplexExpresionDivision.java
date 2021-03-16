package com.company;

import utils.Operation;

public class ComplexExpresionDivision extends ComplexExpression {

    /**
     * Constructor
     * @param arr: NumarComplex[]
     * se seteaza operatia ca si DIVISION
     */
    ComplexExpresionDivision(NumarComplex[] arr){
        super(arr);
        super.setOperatie(Operation.DIVISION);
    }

    /**
     * Metoda pentru executarea unei operatii (de impartire)
     * @param c1: NumarComplex
     * @param c2: NumarComplex
     * @return rez: NumarComplex, rez=c1/c2
     */
    @Override
    public NumarComplex executeOneOperation(NumarComplex c1, NumarComplex c2) {
        return c1.impartire(c2);
    }
}
