package com.tourneynizer.tourneynizer.model;

import org.junit.Test;

public class TournamentTest {

    @Test
    public void create() throws Exception {
        new Tournament("name", "address", null, 1, 1, TournamentType.BRACKET, 1, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullType() throws Exception {
        new Tournament(null, "address", null, 1, 1, null, 1, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullName() throws Exception {
        new Tournament(null, "address", null, 1, 1, TournamentType.BRACKET, 1, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullAddress() throws Exception {
        new Tournament("name", null, null, 1, 1, TournamentType.BRACKET, 1, 0L);
    }


    @Test(expected = IllegalArgumentException.class)
    public void emptyName() throws Exception {
        new Tournament("", "address", null, 1, 1, TournamentType.BRACKET, 1, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void blankName() throws Exception {
        new Tournament("  \n   ", "address", null, 1, 1, TournamentType.BRACKET, 1, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void longName() throws Exception {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < 256; i++) { name.append('f'); }
        new Tournament(name.toString(), "address", null, 1, 1, TournamentType.BRACKET, 1, 0L);
    }


    @Test(expected = IllegalArgumentException.class)
    public void emptyAddress() throws Exception {
        new Tournament("name","", null, 1, 1, TournamentType.BRACKET, 1, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void blankAddress() throws Exception {
        new Tournament("name", "     \n", null, 1, 1, TournamentType.BRACKET, 1, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void longAddress() throws Exception {
        StringBuilder address = new StringBuilder();
        for (int i = 0; i < 256; i++) { address.append('f'); }
        new Tournament("name", address.toString(), null, 1, 1, TournamentType.BRACKET, 1, 0L);
    }

}