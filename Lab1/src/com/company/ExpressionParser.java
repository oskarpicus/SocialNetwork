package com.company;


import utils.NumarComplexParser;
import utils.Operation;

public class ExpressionParser {

    /**
     * Functie pentru validarea unui text ca fiind o expresie de numere complexe
     * @param args: String[]
     * @return true, daca args este valida ; false, altfel
     */
    public boolean validateExpression(String[] args){

        if(args.length<3) //trebuie sa avem macar 2 operanzi si un operator
            return false;

        //validam numerele complexe
        for(int i=0;i<args.length;i+=2){
            if(!NumarComplexParser.validateNumarComplex(args[i]))
                return false;
        }

        //validam operatorii
        String firstOperator=args[1];
        for(int i=1;i<args.length;i+=2) {

            if(!firstOperator.equals(args[i])) //trebuie sa fie acelasi operator
                return false;

            if ((!args[i].equals("+")) && (!args[i].equals("-")) && (!args[i].equals("*")) && (!args[i].equals("/")))
                return false;
        }
        return true;
    }

    /**
     * Functie pentru crearea unui array de numere complexe
     * @param args: String[], args expresie valida
     * @return arr: NumarComplex[], ∀ i indice din arr, arr[i] = NumarComplex(args[i])
     */
    private NumarComplex[] createArray(String[] args){
        NumarComplex[] arr=new NumarComplex[args.length/2+1]; //alocam spatiu pentru toti operanzii
        int contor=0;
        var nrcp=new NumarComplexParser();
        for(int i=0;i<args.length;i+=2)
            arr[contor++]=nrcp.createNumarComplex(args[i]);

        return arr;
    }

    /**
     * Functie pentru determinarea operatorului expresiei
     * @param args: String[], args expresie valida
     * @return op: Operator, determinat pe baza lui args
     */
    private Operation createOperation(String[] args){
        Operation op=null;
        if(args[1].equals("+"))
            op=Operation.ADDITION;
        if(args[1].equals("-"))
            op=Operation.SUBSTRACTION;
        if(args[1].equals("*"))
            op=Operation.MULTIPLICATION;
        if(args[1].equals("/"))
            op=Operation.DIVISION;
        return op;
    }

    /**
     * Functie pentru crearea unei expresii de numere complexe
     * @param args: String[]
     * @return e: ComplexExpression, creat pe baza lui args
     */
    public ComplexExpression makeComplexExpression(String[] args){
        return ExpressionFactory.getInstance().createExpression(createOperation(args), createArray(args));
    }

}
