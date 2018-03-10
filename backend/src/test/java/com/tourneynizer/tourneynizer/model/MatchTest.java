package com.tourneynizer.tourneynizer.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;

public class MatchTest {

    @Test
    public void create() throws Exception {
        MatchChildren children = new MatchChildren(0L, 1L, null, null);
        new Match(0L, children, 0, null, ScoreType.ONE_SET);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullScoreType() throws Exception {
        MatchChildren children = new MatchChildren(0L, 1L, null, null);
        new Match(0L, 0L, children, 0L, 0L, 0L, 0, 0, null, null, null, MatchStatus.CREATED);
    }

    @Test
    public void json1() throws Exception {
        MatchChildren children = new MatchChildren(0L, 1L, null, null);
        Match match =
                new Match(0L, 0L, children, 3L, 0L, 0L, 0, 0, new Timestamp(0), new Timestamp(1), ScoreType.ONE_SET, MatchStatus.CREATED);
        String json = new ObjectMapper().writeValueAsString(match);
        String expected = "{\"id\":0,\"refId\":3,\"score1\":0,\"score2\":0,\"matchChildren\":[{\"id\":\"0\",\"type\":\"team\"},{\"id\":\"1\",\"type\":\"team\"}],\"courtNumber\":0,\"timeStart\":0,\"timeEnd\":1,\"scoreType\":\"ONE_SET\",\"matchStatus\":\"CREATED\",\"tournamentId\":0,\"matchOrder\":0}";
        assertEquals(expected, json);
    }
}