package com.tourneynizer.tourneynizer.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MatchChildrenSerializer extends StdSerializer<MatchChildren> {
    public MatchChildrenSerializer() {
        super(MatchChildren.class);
    }

    @Override
    public void serialize(MatchChildren matchChildren, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        Map<String, Object[]> map = new HashMap<>();

        Long match1Id = matchChildren.getMatchChild1();
        Long match2Id = matchChildren.getMatchChild2();
        Object[] matches = new Object[] {
                match1Id != null ? Collections.singletonMap("id", match1Id) : null,
                match2Id != null ? Collections.singletonMap("id", match2Id) : null
        };


        Long team1Id = matchChildren.getTeamChild1();
        Long team2Id = matchChildren.getTeamChild2();
        Object[] teams = new Object[] {
                team1Id != null ? Collections.singletonMap("id", team1Id) : null,
                team2Id != null ? Collections.singletonMap("id", team2Id) : null
        };

        map.put("matches", matches);
        map.put("teams", teams);

        jsonGenerator.writeObject(map);
    }
}
