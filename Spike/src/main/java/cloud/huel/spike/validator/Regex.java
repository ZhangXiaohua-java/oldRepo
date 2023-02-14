package cloud.huel.spike.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author 张晓华
 * @date 2022-9-3
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {RegexValidator.class})
public @interface Regex {

	String message() default "{cloud.huel.spike.validator.Regex.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	String regex() default "";

}
