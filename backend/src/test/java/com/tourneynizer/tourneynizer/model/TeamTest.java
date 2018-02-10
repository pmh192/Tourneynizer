package com.tourneynizer.tourneynizer.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TeamTest {
    @Test
    public void create() throws Exception {
        new Team("name", 0L, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullTimeCreated() throws Exception {
        new Team(0L, "name", null, 0, 0, true);
    }
}