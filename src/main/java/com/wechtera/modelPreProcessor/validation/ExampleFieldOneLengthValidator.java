package com.wechtera.modelPreProcessor.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.wechtera.modelPreProcessor.model.ModelSpecificInput;
import com.wechtera.modelPreProcessor.util.JSONHelperMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExampleFieldOneLengthValidator implements ModelInputValidator {

    private final Logger logger = LoggerFactory.getLogger(ExampleFieldOneLengthValidator.class);
    private final JSONHelperMethods jsonHelperMethods;

    public ExampleFieldOneLengthValidator(JSONHelperMethods jsonHelperMethods) {
        this.jsonHelperMethods = jsonHelperMethods;
    }

    @Override
    public boolean validate(ModelSpecificInput input) {
        JsonNode field = (JsonNode) input.getField("Field_1");
        if (field == null) {
            return false;
        }

        double[][] values = jsonHelperMethods.extractDouble2DArray(field);

        logger.info("Made it to point 4");

        return values != null && values.length == 1 && values[0].length == 3;
    }

}
