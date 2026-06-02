package com.stephen.httpserver.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

// This class can be used for JSON parsing and handling in the future
public class Json {
    private static ObjectMapper objectMapper = defaultObjectMapper();

    private static ObjectMapper defaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        /*
         * Configure the ObjectMapper to ignore unknown properties during
         * deserialization. This allows us to safely ignore any extra fields in the JSON
         * that are not mapped to our Java classes.
         */
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    public static JsonNode parse(String jsonString) throws Exception {
        return objectMapper.readTree(jsonString);
    }

    public static <T> T fromJson(JsonNode jsonNode, Class<T> valueType) throws Exception {
        return objectMapper.treeToValue(jsonNode, valueType);
    }

    public static JsonNode toJson(Object object) throws Exception {
        return objectMapper.valueToTree(object);
    }

    public static String stringify(JsonNode jsonNode) throws Exception {
        return generateJson(jsonNode, false);
    }

    public static String stringifyPretty(JsonNode jsonNode) throws Exception {
        return generateJson(jsonNode, true);
    }

    public static String generateJson(Object object, boolean prettyPrint) throws Exception {
        ObjectWriter writer = objectMapper.writer();
        if (prettyPrint) {
            writer = writer.with(SerializationFeature.INDENT_OUTPUT);
        }
        return writer.writeValueAsString(object);
    }
}
