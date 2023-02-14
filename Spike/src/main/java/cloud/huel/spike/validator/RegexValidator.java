package cloud.huel.spike.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author 张晓华
 * @date 2022-9-3
 */
public class RegexValidator implements ConstraintValidator<Regex,Long> {

	private volatile String regex;
	private volatile Pattern pattern;

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		synchronized (Object.class) {
			return pattern.matcher(value + "").matches();
		}
	}


	@Override
	public void initialize(Regex constraintAnnotation) {
		regex = constraintAnnotation.regex();
		pattern = Pattern.compile(regex);
	}


}
