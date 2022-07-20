package io.v47.tmdb.http.tck;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer;

import java.io.IOException;

public class TckResultDeserializer extends StdNodeBasedDeserializer<TckResult> {
    public TckResultDeserializer() {
        super(TckResult.class);
    }

    @Override
    public TckResult convert(JsonNode root, DeserializationContext ctxt) throws IOException {
        Class<?> actualType;
        if (root.hasNonNull("failedTests")) {
            actualType = TckResult.Failure.class;
        } else {
            actualType = TckResult.Success.class;
        }

        JavaType jacksonType = ctxt.getTypeFactory().constructType(actualType);
        JsonDeserializer<?> deserializer = ctxt.findRootValueDeserializer(jacksonType);
        JsonParser nodeParser = root.traverse(ctxt.getParser().getCodec());
        nodeParser.nextToken();

        return (TckResult) deserializer.deserialize(nodeParser, ctxt);
    }
}
