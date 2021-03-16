package com.company;

public class Main {

    public static void main(String[] args) {
        ExpressionParser exp_pars=new ExpressionParser();
        if(!exp_pars.validateExpression(args)) {
            System.out.println("Expresie invalida");
            return;
        }
        ComplexExpression expresie=exp_pars.makeComplexExpression(args);
        NumarComplex result=expresie.execute();
        System.out.println(result.toString());
    }
}
