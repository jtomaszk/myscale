package com.jtomaszk.apps.myscale.importer;

import com.google.common.io.CharStreams;

import org.junit.Test;

import java.io.InputStreamReader;

import static org.junit.Assert.*;

/**
 * Created by jarema-user on 2016-01-20.
 */
public class ImporterTest {


    @Test
    public void shouldWork() throws Exception {
        String xml = CharStreams.toString(
                new InputStreamReader(this.getClass().getResourceAsStream("input.csv"), "UTF-8"));

        assertEquals("", xml);
    }
}