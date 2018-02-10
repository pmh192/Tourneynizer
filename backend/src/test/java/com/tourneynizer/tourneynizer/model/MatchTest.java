package com.tourneynizer.tourneynizer.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class MatchTest {

    @Test
    public void create() throws Exception {
        new Match(0L, 0L, 0L, 0L, 0, 0, 0, 0, null, null, ScoreType.ONE_SET);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullScoreType() throws Exception {
        new Match(0L, 0L, 0L, 0L, 0, 0, 0, 0, null, null, null);
    }
}