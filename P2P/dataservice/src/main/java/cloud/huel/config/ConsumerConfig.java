package cloud.huel.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author 张晓华
 * @date 2022-7-15
 */
@Configuration
public class ConsumerConfig {

	/**
	 * 创建RedisTemplate对象
	 *
	 * @param connectionFactory 工厂对象
	 * @return RedisTemplate
	 */
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
		redisTemplate.setKeySerializer(RedisSerializer.string());
		redisTemplate.setHashKeySerializer(RedisSerializer.string());
		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashValueSerializer(serializer);
		redisTemplate.setConnectionFactory(connectionFactory);
		return redisTemplate;
	}


}
