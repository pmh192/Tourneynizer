package com.tourneynizer.tourneynizer.model;

import org.junit.Test;

public class TournamentTest {

    @Test
    public void create() throws Exception {
        new Tournament("name", "address", null, 1, 1, null, 1, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullName() throws Exception {
        new Tournament(null, "address", null, 1, 1, null, 1, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullAddress() throws Exception {
        new Tournament("name", null, null, 1, 1, null, 1, 0L);
    }


    @Test(expected = IllegalArgumentException.class)
    public void emptyName() throws Exception {
        new Tournament("", "address", null, 1, 1, null, 1, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void blankName() throws Exception {
        new Tournament("  \n   ", "address", null, 1, 1, null, 1, 0L);
    }


    @Test(expected = IllegalArgumentException.class)
    public void emptyAddress() throws Exception {
        new Tournament("name","", null, 1, 1, null, 1, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void blankAddress() throws Exception {
        new Tournament("name", "     \n", null, 1, 1, null, 1, 0L);
    }
}