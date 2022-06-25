package com.jazzteam.cleopatra.component.util;

import com.jazzteam.cleopatra.component.base.FormElement;
import com.jazzteam.cleopatra.component.util.constant.ErrorMessages;
import com.jazzteam.cleopatra.component.util.constant.ValidatorsType;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public final class ElementConsumerUtil {

    private static final Map<ValidatorsType, Consumer<FormElement>> stringConsumerMap = Map.of(
            ValidatorsType.NOT_NULL_TEXT_AND_LESS_THAN_100, setValidIfNotBlankAndLessThan(100),
            ValidatorsType.NOT_NULL_NUMERIC_TEXT, setValidIfValueNotNullAndIsIntNumber()
    );

    private ElementConsumerUtil() throws IllegalAccessException {
        throw new IllegalAccessException(ErrorMessages.INIT_STATIC_CLASS_ERROR.getMessage());
    }

    public static Consumer<FormElement> getFormElementConsumer(ValidatorsType validatorType) {
        return stringConsumerMap.get(validatorType);
    }

    private static boolean isDouble(String text) {
        if (text == null) {
            return false;
        }
        try {
            Double.parseDouble(text);
        } catch (NumberFormatException exception) {
            log.debug(ErrorMessages.NUMERIC_FORMAT_ERROR.getMessage() + exception.getMessage());
            return false;
        }
        return true;
    }

    private static boolean isInteger(String text) {
        if (text == null) {
            return false;
        }
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException exception) {
            log.debug(ErrorMessages.NUMERIC_FORMAT_ERROR.getMessage() + exception.getMessage());
            return false;
        }
        return true;
    }

    private static Consumer<FormElement> setValidIfNotBlankAndLessThan(int number) {
        return formElement -> {
            formElement.setValid(formElement.getValue() != null &&
                    !String.valueOf(formElement.getValue()).isBlank() &&
                    String.valueOf(formElement.getValue()).trim().length() <= number);
        };
    }

    private static Consumer<FormElement> setValidIfValueNotNullAndIsIntNumber() {
        return formElement -> {
            formElement.setValid(formElement.getValue() != null &&
                    !String.valueOf(formElement.getValue()).isBlank() &&
                    isInteger(String.valueOf(formElement.getValue())));
        };
    }
}


