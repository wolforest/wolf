package cn.coderule.framework.layer.api.validation.validator;

import cn.coderule.framework.layer.api.validation.ChinesePhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ChinesePhoneNumberValidator implements ConstraintValidator<ChinesePhoneNumber, String> {
    private static final String PHONE_NUMBER_PATTERN = "^1[3-9]\\d{9}$";

    @Override
    public void initialize(ChinesePhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        return Pattern.matches(PHONE_NUMBER_PATTERN, phoneNumber);
    }
}
