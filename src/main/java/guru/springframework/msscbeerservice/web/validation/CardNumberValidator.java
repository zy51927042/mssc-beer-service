package guru.springframework.msscbeerservice.web.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class CardNumberValidator implements ConstraintValidator<CardValidation, Object> {
    @Override
    public void initialize(CardValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if(ObjectUtils.isEmpty(o)){
            log.error("Monitor value is Null");
        }
        return false;

    }
}
