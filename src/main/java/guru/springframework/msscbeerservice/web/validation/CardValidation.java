package guru.springframework.msscbeerservice.web.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CardNumberValidator.class})

public @interface CardValidation {
    String message() default "illegal card number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
