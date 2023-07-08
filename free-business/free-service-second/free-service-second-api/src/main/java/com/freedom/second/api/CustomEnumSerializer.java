package com.freedom.second.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.freedom.second.api.ao.FirstEnum;

import java.io.IOException;

public class CustomEnumSerializer extends JsonDeserializer<FirstEnum> {
    @Override
    public FirstEnum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        int code = node.get("code").asInt();
        for (FirstEnum value : FirstEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid FirstEnum code: " + code);
    }
}
