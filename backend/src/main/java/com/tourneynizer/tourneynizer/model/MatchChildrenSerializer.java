package com.tourneynizer.tourneynizer.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class MatchChildrenSerializer extends StdSerializer<MatchChildren> {
    public MatchChildrenSerializer() {
        super(MatchChildren.class);
    }

    @Override
    public void serialize(MatchChildren matchChildren, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeObject(matchChildren.jsonFirst());
        jsonGenerator.writeObject(matchChildren.jsonSecond());
        jsonGenerator.writeEndArray();
    }
}
