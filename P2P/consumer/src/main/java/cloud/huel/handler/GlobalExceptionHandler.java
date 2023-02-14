package cloud.huel.handler;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author 张晓华
 * @date 2022-7-20
 */
@ControllerAdvice
public class GlobalExceptionHandler {


	@ExceptionHandler({Exception.class})
	public String defaultHandler(Exception e, Model model) {
		model.addAttribute("ex", e);
		return "error";
	}


}
