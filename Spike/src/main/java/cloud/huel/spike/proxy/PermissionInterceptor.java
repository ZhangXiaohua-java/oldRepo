package cloud.huel.spike.proxy;

import cloud.huel.spike.constant.UserConstants;
import cloud.huel.spike.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author 张晓华
 * @date 2022-9-3
 */
@Component
@Slf4j
public class PermissionInterceptor implements HandlerInterceptor {



	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession(false);
		Object obj = null;
		if (Objects.isNull(session) || Objects.isNull(obj = session.getAttribute(UserConstants.USER_KEY))) {
			log.info("拦截请求" + request.getRequestURL());
			response.sendRedirect("/");
			return false;
		}
		return true;
	}



}
