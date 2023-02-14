package cloud.huel.spike.proxy;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;

/**
 * @author 张晓华
 * @date 2022-9-4
 */
@Component
public class ParameterHandler implements HandlerMethodArgumentResolver {

	private DateTimeFormatter formatter;

	public ParameterHandler() {
		this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	}

	// 判断参数的类型是否和当前处理器要注入的参数类型是否匹配,返回false则不会注入,true则会注入
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> cla = parameter.getParameterType();
		return  cla == java.time.format.DateTimeFormatter.class;
	}

	// 向Controller的方法形参注入方法所需要的实参.
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		return formatter;
	}


}
