package com.tourneynizer.tourneynizer.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class MatchChildrenTest {
    @Test
    public void type1() throws Exception {
        MatchChildren children = new MatchChildren(0L, 1L, null, null);
        assertEquals(0L, children.first());
        assertEquals(1L, children.second());
    }

    @Test
    public void type2() throws Exception {
        MatchChildren children = new MatchChildren(0L, null, 1L, null);
        assertEquals(0L, children.first());
        assertEquals(1L, children.second());

        children = new MatchChildren(0L, null, null, 1L);
        assertEquals(0L, children.first());
        assertEquals(1L, children.second());

        children = new MatchChildren(null, 0L, null, 1L);
        assertEquals(0L, children.first());
        assertEquals(1L, children.second());
    }

    @Test
    public void type3() throws Exception {
        MatchChildren children = new MatchChildren(null, null,0L, 1L);
        assertEquals(0L, children.first());
        assertEquals(1L, children.second());
    }

    @Test
    public void json() throws Exception {
        MatchChildren children = new MatchChildren(0L, null, 1L, null);
        String json = new ObjectMapper().writeValueAsString(children);
        String expected = "[{\"id\":\"0\",\"type\":\"team\"},{\"id\":\"1\",\"type\":\"match\"}]";

        assertEquals(expected, json);
    }
}