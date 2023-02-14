package cloud.huel.spike.controller;


import cloud.huel.spike.constant.UserConstants;
import cloud.huel.spike.domain.Login;
import cloud.huel.spike.domain.User;
import cloud.huel.spike.service.IUserService;
import cloud.huel.spike.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 张晓华
 * @since 2022-09-03
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService userService;

	@PostMapping("/login")
	public ResultVO login(@Validated(Login.class)User user, HttpSession session) {
		ResultVO result = userService.login(user);
		if (result.getCode() == 200) {
			user = (User) result.getResultMap().get("user");
			session.setAttribute(UserConstants.USER_KEY, user);
			result.getResultMap().remove("user");
		}
		return result;
	}




}
