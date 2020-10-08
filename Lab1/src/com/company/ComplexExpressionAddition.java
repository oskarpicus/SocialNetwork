package com.company;

import utils.Operation;

public class ComplexExpressionAddition extends ComplexExpression{

    /**
     * Constructor
     * @param arr: NumarComplex[]
     * se seteaza operatia ca si ADDITION
     */
    ComplexExpressionAddition(NumarComplex[] arr){
        super(arr);
        super.setOperatie(Operation.ADDITION);
    }

    /**
     * Metoda pentru executarea unei operatii (de adunare)
     * @param c1: NumarComplex
     * @param c2: NumarComplex
     * @return rez: NumarComplex, rez=c1+c2
     */
    @Override
    public NumarComplex executeOneOperation(NumarComplex c1, NumarComplex c2) {
        return c1.adunare(c2);
    }

}
