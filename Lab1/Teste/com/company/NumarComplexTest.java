package com.company;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumarComplexTest {

    @Test
    void adunare() {
        NumarComplex c1=new NumarComplex(5,6);
        NumarComplex c2=new NumarComplex(7,1);
        c1.adunare(c2);
        assertEquals(c1.get_real(),12);
        assertEquals(c1.get_imaginar(),7);
        NumarComplex c3=new NumarComplex(-4,2);
        c1.adunare(c3);
        assertEquals(c1.get_real(),8);
        assertEquals(c1.get_imaginar(),9);
        c1.adunare(new NumarComplex(0,0));
        assertEquals(c1.get_real(),8);
        assertEquals(c1.get_imaginar(),9);
        c1.adunare(new NumarComplex(4,-5));
        assertEquals(c1.get_real(),12);
        assertEquals(c1.get_imaginar(),4);
    }

    @Test
    void scadere() {
        NumarComplex c1=new NumarComplex(5,6);
        NumarComplex c2=new NumarComplex(7,1);
        c1.scadere(c2);
        assertEquals(c1.get_real(),-2);
        assertEquals(c1.get_imaginar(),5);
        var c3=new NumarComplex(-4,2);
        c2.scadere(c3);
        assertEquals(c2.get_real(),11);
        assertEquals(c2.get_imaginar(),-1);
    }

    @Test
    void inmultire() {
        NumarComplex c1=new NumarComplex(5,6);
        NumarComplex c2=new NumarComplex(7,1);
        c1.inmultire(c2);
        assertEquals(c1.get_real(),29);
        assertEquals(c1.get_imaginar(),47);
        var c3=new NumarComplex(-3,4);
        c2.inmultire(c3);
        assertEquals(c2.get_imaginar(),25);
        assertEquals(-25,c2.get_real());
    }

    @Test
    void impartire() {
        NumarComplex c1=new NumarComplex(5,6);
        NumarComplex c2=new NumarComplex(0,1);
        c1.impartire(c2);
        assertEquals(6,c1.get_real());
        assertEquals(-5,c1.get_imaginar());
        c1.inmultire(c2);
        c1.impartire(new NumarComplex(1,1));
        assertEquals(5.5,c1.get_real());
        assertEquals(0.5,c1.get_imaginar());
        var c3=new NumarComplex(10,5);
        c3.impartire(new NumarComplex(2,6));
        assertEquals(1.25,c3.get_real());
        assertEquals(-1.25,c3.get_imaginar());
    }

    @Test
    void get_real() {
        NumarComplex c=new NumarComplex(5,6);
        assertEquals(c.get_real(),5);
    }

    @Test
    void get_imaginar() {
        NumarComplex c=new NumarComplex(5,6);
        assertEquals(c.get_imaginar(),6);
    }

    @Test
    void conjugat(){
        var c=new NumarComplex(-4,4);
        assertEquals(c.conjugat(),new NumarComplex(-4,-4));
        c=new NumarComplex(3,-3);
        assertEquals(c.conjugat(),new NumarComplex(3,3));
    }

}