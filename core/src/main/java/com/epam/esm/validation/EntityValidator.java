package com.epam.esm.validation;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.NewOrderDto;
import com.epam.esm.dto.RegistrationUserDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.ExceptionCode;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class EntityValidator {

    private final BasicValidator basicValidator;
    private Set<String> certificateFieldNames;

    {
        certificateFieldNames = new HashSet<>();
        certificateFieldNames.add(Constant.NAME_FIELD);
        certificateFieldNames.add(Constant.DESCRIPTION_FIELD);
        certificateFieldNames.add(Constant.PRICE_FIELD);
        certificateFieldNames.add(Constant.DURATION_FIELD);
        certificateFieldNames.add(Constant.TAGS_FIELD);
    }

    public void validateTag(TagDto tagDto) {
        basicValidator.validateNonNull(tagDto, Tag.class.getSimpleName());
        basicValidator.validateStringField(tagDto.getName(), Constant.TAG_NAME);
    }

    public void validateCertificate(CertificateDto certificateDto) {
        basicValidator.validateNonNull(certificateDto, CertificateDto.class.getName());
        basicValidator.validateStringField(certificateDto.getName(), Constant.CERTIFICATE_NAME);
        basicValidator.validateStringField(certificateDto.getDescription(), Constant.DESCRIPTION_FIELD);
        validatePrice(certificateDto.getPrice());
        validateDuration(certificateDto.getDuration());
    }


    public void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new ValidationException(ExceptionCode.CANNOT_BE_NULL.getErrorCode(), Constant.PRICE_FIELD);
        }
        if (price.doubleValue() <= 0) {
            throw new ValidationException(ExceptionCode.SHOULD_BE_POSITIVE.getErrorCode(),
                    Constant.PRICE_FIELD + Constant.EQUAL_SIGN + price);
        }
        if (price.doubleValue() > 999999999.99) {
            throw new ValidationException(ExceptionCode.PRICE_TOO_HIGH.getErrorCode(),
                    Constant.PRICE_FIELD + Constant.EQUAL_SIGN + price);
        }
    }

    public void validateDuration(Integer duration) {
        if (duration ==null){
            throw new ValidationException(ExceptionCode.CANNOT_BE_NULL.getErrorCode(), Constant.DURATION_FIELD);
        }
        if (duration <= 0) {
            throw new ValidationException(
                    ExceptionCode.SHOULD_BE_POSITIVE.getErrorCode(),
                    Constant.DURATION_FIELD + Constant.EQUAL_SIGN + duration);
        }
        if (duration > 366) {
            throw new ValidationException(
                    ExceptionCode.DURATION_CANNOT_BE_MORE_THAN_YEAR.getErrorCode(),
                    Constant.DURATION_FIELD + Constant.EQUAL_SIGN + duration);
        }
    }

    public void validateEmail(String email){
        if (!email.matches("^\\w*@\\w*\\.[a-z]*$")){
            throw new ValidationException(
                    ExceptionCode.INVALID_EMAIL.getErrorCode(),
                    Constant.EMAIL_FIELD+ Constant.EQUAL_SIGN + email);
        }
    }

    public void validateName(String name){
        if (!name.matches("^[A-ZА-Яa-zа-я]*$")){
            throw new ValidationException(
                    ExceptionCode.INVALID_NAME.getErrorCode(),
                    Constant.NAME_FIELD+ Constant.EQUAL_SIGN + name);
        }
    }

    public void validateSurname(String name){
        if (!name.matches("^[A-ZА-Яa-zа-я-]*$")){
            throw new ValidationException(
                    ExceptionCode.INVALID_SURNAME.getErrorCode(),
                    Constant.SURNAME_FIELD+ Constant.EQUAL_SIGN + name);
        }
    }


    public void validateOrder(NewOrderDto newOrderDto) {
        basicValidator.validateNonNull(newOrderDto.getUserId(), Constant.USER_ID);
        basicValidator.validateNonNull(newOrderDto.getCertificatesId(), Constant.CERTIFICATES_ID);
        basicValidator.validateIdIsPositive(newOrderDto.getUserId());
        if (newOrderDto.getCertificatesId().isEmpty()) {
            throw new ValidationException(ExceptionCode.CANNOT_BE_EMPTY.getErrorCode(), Constant.CERTIFICATES_ID);
        }
    }

    public void validateRegistrationUser(RegistrationUserDto registrationUserDto){
        basicValidator.validateNonNull(registrationUserDto.getName(), Constant.NAME_FIELD);
        basicValidator.validateNonNull(registrationUserDto.getSurname(),Constant.SURNAME_FIELD);
        basicValidator.validateNonNull(registrationUserDto.getEmail(),Constant.EMAIL_FIELD);
        basicValidator.validateNonNull(registrationUserDto.getPassword(),Constant.PASSWORD_FIELD);
        validateEmail(registrationUserDto.getEmail());
        validateName(registrationUserDto.getName());
        validateSurname(registrationUserDto.getSurname());
    }


    private static class Constant {
        private static final String NAME_FIELD = "name";
        private static final String SURNAME_FIELD = "surname";
        private static final String EMAIL_FIELD = "email";
        private static final String PASSWORD_FIELD = "password";
        private static final String DESCRIPTION_FIELD = "description";
        private static final String TAGS_FIELD = "tags";
        private static final String DURATION_FIELD = "duration";
        private static final String PRICE_FIELD = "price";
        private static final String EQUAL_SIGN = " = ";
        private static final String TAG_NAME = "tag name";
        private static final String CERTIFICATE_NAME = "certificate name";
        private static final String USER_ID = "userId";
        private static final String CERTIFICATES_ID = "certificatesId";
    }
}
