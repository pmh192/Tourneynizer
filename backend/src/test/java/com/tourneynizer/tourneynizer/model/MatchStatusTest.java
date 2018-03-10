package com.tourneynizer.tourneynizer.model;

import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MatchStatusTest {

    @Test
    public void allPresent() throws Exception {
        MatchStatus[] statuses = MatchStatus.values();
        Map<MatchStatus, String> map = MatchStatus.getStringMap();
        Set<MatchStatus> set = map.keySet();

        assertEquals(statuses.length, set.size());

        for (MatchStatus m : statuses) {
            assertNotNull(map.get(m));
        }
    }
}