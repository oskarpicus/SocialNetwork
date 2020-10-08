package com.company;

import utils.Operation;

public class ExpressionFactory {

    private static final ExpressionFactory instanta=new ExpressionFactory();

    private ExpressionFactory() { }

    /**
     * Getter pentru un obiect de tipul ExpressionFactory
     * @return instanta: ExpressionFactory
     */
    public static ExpressionFactory getInstance(){
        return instanta;
    }

    /**
     * Functie pentru crearea unei expresii de numere complexe
     * @param operation: Operation
     * @param args: NumarComplex[]
     * @return c: ComplexExpression, c.operation=operation, c.array=args
     */
    public ComplexExpression createExpression(Operation operation,NumarComplex[] args){

        switch (operation){
            case ADDITION -> {
                return new ComplexExpressionAddition(args);
            }
            case SUBSTRACTION -> {
                return new ComplexExpressionSubstraction(args);
            }
            case MULTIPLICATION -> {
                return new ComplexExpressionMultiplication(args);
            }
            case DIVISION -> {
                return new ComplexExpresionDivision(args);
            }
            default -> {
                return null;
            }
        }
    }
}
