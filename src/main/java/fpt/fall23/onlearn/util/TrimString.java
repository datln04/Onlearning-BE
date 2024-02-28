package fpt.fall23.onlearn.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class TrimString extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctx)
            throws IOException {
        String str = p.getText();
        try {
            return str.trim();
        } catch (Exception e) {
            return null;
        }
    }
}
