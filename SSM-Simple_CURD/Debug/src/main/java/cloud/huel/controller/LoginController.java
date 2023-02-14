package cloud.huel.controller;

import cloud.huel.domain.Admin;
import cloud.huel.domain.Message;
import cloud.huel.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author 张晓华
 * @version 1.0
 */
@Controller
public class LoginController {
	private static final Integer NORMAL_STATUS = 1;
	@Autowired
	private LoginService service;

	@PostMapping("/login")
	public String login(@Valid Admin admin, HttpSession session, BindingResult result){
		if (!result.hasFieldErrors()) {
			Admin adminLogin = service.adminLogin(admin);
			if (adminLogin == null) {
				session.setAttribute("tip","登录失败,请检查输入的参数");
				return "redirect:/login.jsp";
			}
			Integer status = adminLogin.getStatus();
			if (status != NORMAL_STATUS) {
				session.setAttribute("tip","账号已经被冻结,无权限进行操作");
				return "redirect:/login.jsp";
			}
			session.setAttribute("user",adminLogin);
			System.out.println(adminLogin);
			return "emps";
		}
		return null;

	}

	@GetMapping("/login")
	public String resolveBug(){
		return "forward:/toEmp";
	}

	@GetMapping("/logout")
	public String logout(HttpSession httpSession){
		String contextPath = httpSession.getServletContext().getContextPath();
		httpSession.setAttribute("user",null);
		httpSession.invalidate();
		return "redirect:"+contextPath+"/login.jsp";
	}


}
