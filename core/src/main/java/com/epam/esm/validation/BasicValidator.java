package com.epam.esm.validation;

import com.epam.esm.exception.ExceptionCode;
import com.epam.esm.exception.ValidationException;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

@Component
public class BasicValidator {

    private final Set<String> orderByValues;

    {
        orderByValues = new HashSet<>();
        orderByValues.add(Constants.SORT_BY_DATE_ASC);
        orderByValues.add(Constants.SORT_BY_DATE_DESC);
        orderByValues.add(Constants.SORT_BY_NAME_ASC);
        orderByValues.add(Constants.SORT_BY_NAME_DESC);
    }

    public void checkTagNameParams(Map<String, String> params) {
        if (params.containsKey(Constants.TAG_PARAM)) {
            params.replace(Constants.TAG_PARAM,
                    validateTagNames(params.get(Constants.TAG_PARAM)));
        }
    }

    private String validateTagNames(String tagNamesAsString) {
        List<String> validatedTags = new ArrayList<>();
        String[] tagNames = tagNamesAsString.split(Constants.COMMA);
        for (String tagName : tagNames) {
            validateStringField(tagName, Constants.TAG_NAME);
            validatedTags.add(tagName.trim().toLowerCase());
        }
        return String.join(Constants.COMMA, validatedTags);
    }

    public void checkOrderByParams(Map<String, String> params) {
        if (params.containsKey(Constants.ORDER_BY)) {
            validateOrderByValue(params.get(Constants.ORDER_BY));
        }
    }

    private void validateOrderByValue(String value) {
        if (!orderByValues.contains(value.toLowerCase().trim())) {
            throw new ValidationException(ExceptionCode.INVALID_ORDER_BY_VALUE.getErrorCode(), value);
        }
    }

    public void validateIdIsPositive(long id) {
        if (id <= 0) {
            throw new ValidationException(ExceptionCode.SHOULD_BE_POSITIVE.getErrorCode(),
                    Constants.ID + Constants.EQUAL_SIGN + id);
        }
    }

    public void validateNonNull(Object object, String value) {
        if (object == null) {
            throw new ValidationException(ExceptionCode.CANNOT_BE_NULL.getErrorCode(), value);
        }
    }

    public void validateStringField(String string, String field) {
        if (!StringUtils.hasText(string)) {
            throw new ValidationException(ExceptionCode.CANNOT_BE_EMPTY.getErrorCode(), field);
        }
    }

    public void validateOrderParams(Map<String, String> params) {
        if (params.containsKey(Constants.USER_ID)&&StringUtils.hasText(params.get(Constants.USER_ID))){

            if (NumberUtils.isDigits(params.get(Constants.USER_ID))) {
                validateIdIsPositive(Long.parseLong(params.get(Constants.USER_ID)));
            }else{
                throw new ValidationException("50104", "userId");
            }
        }else {
            params.remove(Constants.USER_ID);
        }
    }

    private static class Constants {
        private static final String SORT_BY_NAME_ASC = "name";
        private static final String SORT_BY_NAME_DESC = "-name";
        private static final String SORT_BY_DATE_ASC = "date";
        private static final String SORT_BY_DATE_DESC = "-date";
        private static final String ORDER_BY = "orderBy";
        private static final String ID = "id";
        private static final String EQUAL_SIGN = " = ";
        private static final String COMMA = ",";
        private static final String TAG_PARAM = "tag";
        private static final String TAG_NAME = "tag name";
        private static final String USER_ID = "userId";
    }
}
