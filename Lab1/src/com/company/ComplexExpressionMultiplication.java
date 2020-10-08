package com.company;

import utils.Operation;

public class ComplexExpressionMultiplication extends ComplexExpression{

    /**
     * Constructor
     * @param arr: NumarComplex[]
     * se seteaza operatia ca si MULTIPLICATION
     */
    ComplexExpressionMultiplication(NumarComplex[] arr){
        super(arr);
        this.setOperatie(Operation.MULTIPLICATION);
    }

    /**
     * Metoda pentru executarea unei operatii (de inmultire)
     * @param c1: NumarComplex
     * @param c2: NumarComplex
     * @return rez: NumarComplex, rez=c1*c2
     */
    @Override
    public NumarComplex executeOneOperation(NumarComplex c1, NumarComplex c2) {
        return c1.inmultire(c2);
    }
}
