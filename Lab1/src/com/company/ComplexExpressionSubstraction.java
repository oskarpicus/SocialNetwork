package com.company;

import utils.Operation;

public class ComplexExpressionSubstraction extends ComplexExpression{

    /**
     * Constructor
     * @param arr: NumarComplex[]
     * se seteaza operatia ca si SUBSTRACTION
     */
    ComplexExpressionSubstraction(NumarComplex[] arr){
        super(arr);
        super.setOperatie(Operation.SUBSTRACTION);
    }

    /**
     * Metoda pentru executarea unei operatii (de scadere)
     * @param c1: NumarComplex
     * @param c2: NumarComplex
     * @return rez: NumarComplex, rez=c1-c2
     */
    @Override
    public NumarComplex executeOneOperation(NumarComplex c1, NumarComplex c2) {
        return c1.scadere(c2);
    }
}
