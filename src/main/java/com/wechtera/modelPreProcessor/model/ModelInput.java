package com.wechtera.modelPreProcessor.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ModelInput {
    protected JsonNode jsonNode;
    private final Logger logger = LoggerFactory.getLogger(ModelInput.class);

    // No-argument constructor for Jackson
    public ModelInput() {
        // Initialize fields if needed
    }

    public ModelInput(JsonNode jsonNode) throws Exception {
        logger.warn("WTF IS GOING ON : ");
        logger.info(jsonNode.toString());
        this.jsonNode = jsonNode;
    }

    // Extracts the field from the JsonNode
    public abstract Object getField(String fieldName);

}