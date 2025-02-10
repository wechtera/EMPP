package com.wechtera.modelPreProcessor.validation;

import com.wechtera.modelPreProcessor.model.ModelSpecificInput;
import com.wechtera.modelPreProcessor.util.JSONHelperMethods;


/**
 * create new components to implement this interface to write
 * validation scripts
 */
public interface ModelInputValidator {
    public abstract boolean validate(ModelSpecificInput input);
}
