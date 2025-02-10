package com.wechtera.modelPreProcessor.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.wechtera.modelPreProcessor.validation.ExampleFieldOneLengthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the package that users should configure based on specific
 * model neeeds.
 */
public class ModelSpecificInput extends ModelInput{

    private final Logger logger = LoggerFactory.getLogger(ModelSpecificInput.class);


    // No-argument constructor for Jackson
    public ModelSpecificInput() throws Exception {
        super(null);
    }

    // Constructor used by Jackson with the @JsonCreator and @JsonProperty annotations
    @JsonCreator
    public ModelSpecificInput(@JsonProperty("json") JsonNode jsonNode) throws Exception {
        super(jsonNode);  // Pass the JSON Node content to the super class constructor
        this.jsonNode = jsonNode;  // Directly set the JsonNode after parsing it
    }

    /**
     * Knowledge of the json structure is necessary, an example
     * config or included avro in the repo is suggested
     * @param fieldName - Name of json object field to retrieve
     * @return - Json objeect value - MUST BE CAST APPROPRIATELY
     */
    @Override
    public Object getField(String fieldName) {
        logger.info("Made it to point 5");

        return jsonNode.get(fieldName);
    }
}
