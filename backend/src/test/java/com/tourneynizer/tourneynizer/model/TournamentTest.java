package com.tourneynizer.tourneynizer.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TournamentTest {

    @Test
    public void create() throws Exception {
        new Tournament("name", "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullType() throws Exception {
        new Tournament(null, "address", null, 1, 1, null, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullName() throws Exception {
        new Tournament(null, "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullAddress() throws Exception {
        new Tournament("name", null, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 0L);
    }


    @Test(expected = IllegalArgumentException.class)
    public void emptyName() throws Exception {
        new Tournament("", "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void blankName() throws Exception {
        new Tournament("  \n   ", "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void longName() throws Exception {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < 256; i++) { name.append('f'); }
        new Tournament(name.toString(), "address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 0L);
    }


    @Test(expected = IllegalArgumentException.class)
    public void emptyAddress() throws Exception {
        new Tournament("name","", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void blankAddress() throws Exception {
        new Tournament("name", "     \n", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void longAddress() throws Exception {
        StringBuilder address = new StringBuilder();
        for (int i = 0; i < 256; i++) { address.append('f'); }
        new Tournament("name", address.toString(), null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 0L);
    }

    @Test
    public void json() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Tournament t = new Tournament("name","address", null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 0L);
        String json = objectMapper.writeValueAsString(t);

        String expected = "{\"id\":null,\"name\":\"name\",\"address\":\"address\",\"timeCreated\":null,\"startTime\":null,\"teamSize\":1,\"maxTeams\":1,\"type\":\"VOLLEYBALL_BRACKET\",\"creatorId\":0}";

        assertEquals(expected, json);
    }
}