package com.company;

import org.junit.jupiter.api.Test;
import utils.NumarComplexParser;

import static org.junit.jupiter.api.Assertions.*;

class NumarComplexParserTest {

    private final NumarComplexParser nrcp=new NumarComplexParser();

    @Test
    void valideaza_numar_complex(){
        assertTrue(NumarComplexParser.validateNumarComplex("1+3*i"));
        assertFalse(NumarComplexParser.validateNumarComplex("a+b*i"));

        assertFalse(NumarComplexParser.validateNumarComplex("428248a"));
        assertTrue(NumarComplexParser.validateNumarComplex("5"));
        assertTrue(NumarComplexParser.validateNumarComplex("+415"));
        assertTrue(NumarComplexParser.validateNumarComplex("-425"));

        assertTrue(NumarComplexParser.validateNumarComplex("5111*i"));
        assertTrue(NumarComplexParser.validateNumarComplex("-5111*i"));
        assertFalse(NumarComplexParser.validateNumarComplex("51b11*i"));
        assertTrue(NumarComplexParser.validateNumarComplex("i"));
        assertTrue(NumarComplexParser.validateNumarComplex("-i"));
        assertFalse(NumarComplexParser.validateNumarComplex("+j"));
        assertTrue(NumarComplexParser.validateNumarComplex("+i"));

        assertTrue(NumarComplexParser.validateNumarComplex("4+i"));
        assertTrue(NumarComplexParser.validateNumarComplex("4-i"));
        assertTrue(NumarComplexParser.validateNumarComplex("5291-284*i"));
        assertFalse(NumarComplexParser.validateNumarComplex("42+5111*iA"));
    }

    @Test
    void creeaza_numar_complex() {
        assertEquals(new NumarComplex(42,0),nrcp.createNumarComplex("42"));
        assertEquals(new NumarComplex(-42,0),nrcp.createNumarComplex("-42"));

        assertEquals(new NumarComplex(0,42),nrcp.createNumarComplex("42*i"));
        assertEquals(new NumarComplex(0,-53),nrcp.createNumarComplex("-53*i"));

        assertEquals(new NumarComplex(42,1),nrcp.createNumarComplex("42+i"));
        assertEquals(new NumarComplex(42,-1),nrcp.createNumarComplex("42-i"));

        assertEquals(new NumarComplex(0,1),nrcp.createNumarComplex("i"));
        assertEquals(new NumarComplex(0,-1),nrcp.createNumarComplex("-i"));

        assertEquals(new NumarComplex(421,24),nrcp.createNumarComplex("421+24*i"));
        assertEquals(new NumarComplex(-4,24),nrcp.createNumarComplex("-4+24*i"));
        assertEquals(new NumarComplex(421,-9),nrcp.createNumarComplex("421-9*i"));
        assertEquals(new NumarComplex(-421,-24),nrcp.createNumarComplex("-421-24*i"));
    }
}
