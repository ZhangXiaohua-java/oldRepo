package cloud.huel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 张晓华
 * @date 2022-7-14
 */
@Configuration
public class BeanConfig implements WebMvcConfigurer {


	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/user/toRegister").setViewName("register");
		registry.addViewController("/user/realName").setViewName("realName");
		registry.addViewController("/user/toRecharge").setViewName("toRecharge");
		//registry.addViewController("/user/recharge").setViewName("charge");
	}


}
