package cloud.huel.spike.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author 张晓华
 * @date 2022-9-9
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AccessLimit {

	int duration() default 60;

	int count() default 5;

}
