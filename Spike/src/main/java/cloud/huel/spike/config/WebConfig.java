package cloud.huel.spike.config;

import cloud.huel.spike.proxy.AccessLimitInterceptor;
import cloud.huel.spike.proxy.ParameterHandler;
import cloud.huel.spike.proxy.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 张晓华
 * @date 2022-9-3
 */
@EnableRedisHttpSession
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private PermissionInterceptor permissionInterceptor;

	@Autowired
	private ParameterHandler parameterHandler;

	@Autowired
	private AccessLimitInterceptor accessLimitInterceptor;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("login");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		accessLimitInterceptor.setRedisTemplate(redisTemplate);
		List<String> list = Stream.of("/bootstrap/**", "/img/**", "/jquery-validation/**", "/js/**", "/layer/**", "/", "/user/login", "/error")
				.collect(Collectors.toList());
		registry.addInterceptor(permissionInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns(list);
		registry.addInterceptor(accessLimitInterceptor).addPathPatterns("/**").excludePathPatterns(list);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisSerializer<String> string = RedisSerializer.string();
		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(string);
		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashKeySerializer(string);
		redisTemplate.setHashValueSerializer(serializer);
		redisTemplate.setExposeConnection(true);
		redisTemplate.setConnectionFactory(connectionFactory);
		return redisTemplate;
	}

	//@Bean
	//public DefaultCookieSerializer cookieSerializer() {
	//	DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
	//	cookieSerializer.setDomainName("127.0.0.1");
	//	cookieSerializer.setCookieName("flag");
	//	cookieSerializer.setCookiePath("/");
	//	cookieSerializer.setCookieMaxAge(24*60*60);
	//	return cookieSerializer;
	//}


	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(parameterHandler);
	}


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler ("/static/**")
				.addResourceLocations("classpath:/static/");
	}


	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedOriginPatterns("*");
	}



}
