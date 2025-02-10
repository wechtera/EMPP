package com.wechtera.modelPreProcessor.validation;


import com.fasterxml.jackson.databind.JsonNode;
import com.wechtera.modelPreProcessor.model.ModelInput;
import com.wechtera.modelPreProcessor.model.ModelSpecificInput;
import com.wechtera.modelPreProcessor.util.JSONHelperMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExampleFieldOneContentValidator implements ModelInputValidator {
    private final Logger logger = LoggerFactory.getLogger(ExampleFieldOneLengthValidator.class);
    private final JSONHelperMethods jsonHelperMethods;


    public ExampleFieldOneContentValidator(JSONHelperMethods jsonHelperMethods) {
        this.jsonHelperMethods = jsonHelperMethods;
    }

    @Override
    public boolean validate(ModelSpecificInput input) {
        JsonNode field = (JsonNode) input.getField("Field_1");
        if (field == null) {
            return false;
        }
        logger.info("values: {}", field);

        double[][] values = jsonHelperMethods.extractDouble2DArray(field);

        logger.info("values: {}", values);

        for(int i =0; i< values[0].length; i++)
            if(values[0][i] > 20)
                return false;

        return true;
    }
}
