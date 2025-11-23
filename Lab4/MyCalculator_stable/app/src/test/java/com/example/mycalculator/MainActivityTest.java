package com.example.mycalculator;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mvel2.sh.Main;

public class MainActivityTest {

    @Test
    public void testEvaluate() throws Exception {
        assertEquals("3", MainActivity.evaluate("2+1"));
        assertEquals("4", MainActivity.evaluate("7-3"));
        assertEquals("9", MainActivity.evaluate("3*3"));
        assertEquals("3.5", MainActivity.evaluate("7/2"));
    }

    @Test
    public void badTestEvaluate() throws Exception {
        assertEquals("4", MainActivity.evaluate("2+1"));
        assertEquals("3", MainActivity.evaluate("7-3"));
        assertEquals("10", MainActivity.evaluate("3*3"));
        assertEquals("3", MainActivity.evaluate("7/2"));
    }
}