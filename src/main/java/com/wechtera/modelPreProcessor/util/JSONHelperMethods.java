package com.wechtera.modelPreProcessor.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public class JSONHelperMethods {


    // Extracts a String from a JsonNode
    public static String extractString(JsonNode jsonNode) {
        return (jsonNode != null && jsonNode.isTextual()) ? jsonNode.asText() : null;
    }

    // Extracts an Integer from a JsonNode
    public static Integer extractInteger(JsonNode jsonNode) {
        return (jsonNode != null && jsonNode.isInt()) ? jsonNode.asInt() : null;
    }

    // Extracts a Long from a JsonNode
    public static Long extractLong(JsonNode jsonNode) {
        return (jsonNode != null && jsonNode.isLong()) ? jsonNode.asLong() : null;
    }

    // Extracts a Float from a JsonNode
    public static Float extractFloat(JsonNode jsonNode) {
        return (jsonNode != null && jsonNode.isFloat()) ? jsonNode.floatValue() : null;
    }

    // Extracts a Double from a JsonNode
    public static Double extractDouble(JsonNode jsonNode) {
        return (jsonNode != null && jsonNode.isDouble()) ? jsonNode.asDouble() : null;
    }

    // Extracts a 1D double array from a JsonNode
    public static double[] extractDoubleArray(JsonNode jsonNode) {
        if (jsonNode != null && jsonNode.isArray()) {
            double[] result = new double[jsonNode.size()];
            for (int i = 0; i < jsonNode.size(); i++) {
                result[i] = jsonNode.get(i).asDouble();
            }
            return result;
        }
        return new double[0]; // Default empty array
    }

    // Extracts a 2D double array from a JsonNode
    public static double[][] extractDouble2DArray(JsonNode jsonNode) {
        if (jsonNode != null && jsonNode.isArray()) {
            double[][] result = new double[jsonNode.size()][];
            for (int i = 0; i < jsonNode.size(); i++) {
                JsonNode innerArray = jsonNode.get(i);
                if (innerArray.isArray()) {
                    result[i] = new double[innerArray.size()];
                    for (int j = 0; j < innerArray.size(); j++) {
                        result[i][j] = innerArray.get(j).asDouble();
                    }
                } else {
                    return new double[0][0]; // Not a valid 2D array
                }
            }
            return result;
        }
        return new double[0][0]; // Default empty 2D array
    }



}
