package cloud.huel.interceptor;

import cloud.huel.domain.Admin;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 张晓华
 * @version 1.0
 */
@Component
public class AccessInterceptor implements HandlerInterceptor {
	private static final Integer NORMAL_STATUS = 1;
	private List<String> uriList = null;

	public AccessInterceptor() {
		this.uriList = new ArrayList<>();
		uriList.add("/emp");
		uriList.add("/emps");
		uriList.add("/toEmp");
		uriList.add("/empNameQuery");
		uriList.add("/dept");
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
							 Object handler) throws Exception {
		String requestPath = request.getServletPath();
		System.out.println(requestPath);
		if (!uriList.contains(requestPath)) {
			return true;
		}
		HttpSession httpSession = null;
		Object obj = null;
		httpSession = request.getSession(false);
		if (httpSession == null) {
			response.sendRedirect(request.getContextPath()+"/login.jsp");
			return false;
		}
		obj = httpSession.getAttribute("user");
		if (obj == null) {
			response.sendRedirect(request.getContextPath()+"/login.jsp");
			return false;
		}
		Admin admin = (Admin) obj;
		Integer status = ((Admin) obj).getStatus();
		if (status == NORMAL_STATUS) {
			return true;
		}
		httpSession.setAttribute("tip","该账户已被冻结,无法进行操作");
		response.sendRedirect(request.getContextPath()+"/login.jsp");
		return false;
	}
}
