package com.tourneynizer.tourneynizer.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class TournamentTest {

    @Test
    public void create() throws Exception {
        new Tournament("name", 5.4, 3.4, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullType() throws Exception {
        new Tournament(null, 5.4, 3.4, null, 1, 1, null, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullName() throws Exception {
        new Tournament(null, 5.4, 3.4, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyName() throws Exception {
        new Tournament("", 5.4, 3.4, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void blankName() throws Exception {
        new Tournament("  \n   ", 5.4, 3.4, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void longName() throws Exception {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < 256; i++) { name.append('f'); }
        new Tournament(name.toString(), 5.4, 3.4, null, 1,1, TournamentType.VOLLEYBALL_BRACKET, 0L);
    }


    @Test
    public void json() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Tournament t = new Tournament("name",5.4, 3.4, null, 1, 1, TournamentType.VOLLEYBALL_BRACKET, 0L);
        String json = objectMapper.writeValueAsString(t);

        String expected = "{\"id\":null,\"name\":\"name\",\"lat\":5.4,\"lng\":3.4,\"timeCreated\":null,\"startTime\":null,\"teamSize\":1,\"maxTeams\":1,\"numCourts\":1,\"type\":\"VOLLEYBALL_BRACKET\",\"creatorId\":0}";
    }
}