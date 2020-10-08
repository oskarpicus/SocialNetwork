package utils;

import com.company.NumarComplex;

public class NumarComplexParser {

    /**
     * Functie pentru verificarea corectitudinii unui numar complex
     * @param str: String
     * @return true, daca str este un numar complex corect scris
     * false, altfel
     */
    public static boolean validateNumarComplex(String str){

        //daca este numar real (parte imaginara = 0)
        if(str.matches("^[+-]?[1-9][0-9]*$")) //ex. 4, -2
            return true;

        //are partea reala egala cu 0
        if(str.matches("^[+-]?[1-9][0-9]*\\*[i]$")) //ex. 2*i, -52*i
            return true;

        //are partea imaginara egala cu 1
        if(str.matches("^[+-]?[1-9][0-9]*[+-]i$"))
            return true;

        // +- i
        if(str.matches("^[+-]*i$"))
            return true;

        //este numar complex a+b*i
        return str.matches("^[+-]?[1-9][0-9]*[+-][1-9][0-9]*\\*[i]?$");
    }

    /**
     * Functie pentru crearea unui numar complex pe baza unui text
     * @param str: String, validateNumarComplex(str)=true
     * @return c: NumarComplex, creat pe baza lui str
     */
    public NumarComplex createNumarComplex(String str){

        //daca este numar real (parte imaginara = 0)
        if(str.matches("^[+-]?[1-9][0-9]*$")){ //ex. -4, 5
            return new NumarComplex(Integer.parseInt(str),0);
        }

        //are partea reala egala cu 0
        if(str.matches("^[+-]?[1-9][0-9]*\\*[i]$")){ //ex. 2*i, -42*i
            return new NumarComplex(0,Integer.parseInt(str.substring(0,str.length()-2))); //-2 pentru a omite *i
        }

        //are partea imaginara egala cu 1
        if(str.matches("^[+-]?[1-9][0-9]*[+-]i$")) { //ex. 42+i,-42-i
            int semn=str.indexOf('+',1);
            int imaginar=0;

            if(semn>-1) //am gasit plus ==> partea imaginara este egala cu 1
                imaginar=1;

            semn= (semn==-1) ? str.indexOf('-',1) : semn;
            imaginar = (imaginar==0) ? -1 : 1;

            String real=str.substring(0,semn);
            return new NumarComplex(Integer.parseInt(real),imaginar);
        }

        // +- i
        if(str.matches("^[+-]*i$")){
            int imaginar = (str.charAt(0)!='-') ? 1 : -1;
            return new NumarComplex(0,imaginar);
        }

        //este numar complex a+b*i
        int semn=str.indexOf('+',1);
        semn= (semn==-1) ? str.indexOf('-',1) : semn;
        String real=str.substring(0,semn);
        String imaginar=str.substring(semn);
        imaginar=imaginar.substring(0,imaginar.length()-2);
        return new NumarComplex(Integer.parseInt(real),Integer.parseInt(imaginar));

    }
}
