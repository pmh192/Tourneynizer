package com.tourneynizer.tourneynizer.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class MatchChildrenTest {
    @Test
    public void json() throws Exception {
        MatchChildren children = new MatchChildren(null, null, 0L, 1L);
        String json = new ObjectMapper().writeValueAsString(children);
        String expected = "{\"teams\":[null,null],\"matches\":[{\"id\":0},{\"id\":1}]}";

        assertEquals(expected, json);
    }

    @Test
    public void json2() throws Exception {
        MatchChildren children = new MatchChildren(null, 3L, 0L, 1L);
        String json = new ObjectMapper().writeValueAsString(children);
        String expected = "{\"teams\":[null,{\"id\":3}],\"matches\":[{\"id\":0},{\"id\":1}]}";

        assertEquals(expected, json);
    }
}