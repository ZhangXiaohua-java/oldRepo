package cloud.huel.spike.proxy;

import cloud.huel.spike.annotation.AccessLimit;
import cloud.huel.spike.constant.UserConstants;
import cloud.huel.spike.domain.User;
import cloud.huel.spike.pub.ResponseStatus;
import cloud.huel.spike.vo.ResultVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;

/**
 * @author 张晓华
 * @date 2022-9-9
 * 计数器限流的简单实现.
 *
 */
@Slf4j
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {


	private RedisTemplate<String, Object> redisTemplate;


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 判断是否是控制器的对象
		if (handler instanceof HandlerMethod) {
			log.info("转型" + handler);
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			AccessLimit annotation = ((HandlerMethod) handler).getMethodAnnotation(AccessLimit.class);
			// 如果被当前拦截器拦截的控制器对象调用的方法没有AccessLimit注解就直接放行
			if (Objects.isNull(annotation)) {
				return true;
			}
			HttpSession session = request.getSession(false);
			User user = (User) session.getAttribute(UserConstants.USER_KEY);
			int count = annotation.count();
			int duration = annotation.duration();
			String uri = request.getRequestURI();
			ValueOperations<String, Object> ops = redisTemplate.opsForValue();
			uri = UserConstants.ACCESS_PREFIX + uri + ":" + user.getId();
			String accessCount = (String) ops.get(uri);
			if (Objects.isNull(accessCount)) {
				ops.set(uri, Integer.valueOf(1).toString(), Duration.ofSeconds(duration));
				return true;
			} else if (Integer.valueOf(accessCount) < count ) {
				ops.increment(uri);
				return true;
			} else {
			//	 返回频繁请求的json数据
				render(response, ResponseStatus.FREQUENT_OPERATION);
				return false;
			}

		}
		return true;
	}


	public void render(HttpServletResponse response, ResponseStatus status) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ObjectMapper mapper = new ObjectMapper();
		String result = mapper.writeValueAsString(ResultVO.error(status));
		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(result.getBytes(StandardCharsets.UTF_8));
		outputStream.flush();
		outputStream.close();
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
}
