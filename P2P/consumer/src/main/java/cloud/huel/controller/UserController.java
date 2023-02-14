package cloud.huel.controller;

import cloud.huel.constant.KeyConstants;
import cloud.huel.domain.user.User;
import cloud.huel.service.UserService;
import cloud.huel.vo.ResponseMessage;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 张晓华
 * @date 2022-7-16
 */
@RestController
@RequestMapping("/user")
public class UserController {


	@DubboReference(version = "1.0", retries = 3, timeout = 2000)
	private UserService userService;

	private static final Pattern pattern = Pattern.compile("^(1(3|5|8|9))([0-9]{9})$");


	@PostMapping("/register")
	public ResponseMessage register(User user, HttpSession session, String code) {
		ResponseMessage message = null;
		Boolean flag = userService.checkCode(user.getPhone(), code);
		if (!flag) {
			return ResponseMessage.error("验证码不一致");
		}
		user = userService.register(user);
		if (user == null) {
			message = ResponseMessage.error("注册失败");
		} else {
			message = ResponseMessage.success();
		}
		session.setAttribute(KeyConstants.USER_SESSION, user);
		return message;
	}


	@GetMapping("/phone/{phone}")
	public ResponseMessage checkPhoneExists(@PathVariable String phone) {
		Boolean flag = userService.phoneExists(phone);
		if (!flag) {
			return ResponseMessage.error("该手机已经被注册");
		} else {
			return ResponseMessage.success();
		}
	}


	@GetMapping("/code/{phone}")
	public ResponseMessage sendCode(@PathVariable String phone) {
		Matcher matcher = pattern.matcher(phone);
		//后端再进行一次手机号格式校验
		if (!matcher.find()) {
			return ResponseMessage.error("手机号不格式不正确");
		}
		//请求发送验证码
		Boolean flag = userService.sendCode(phone);
		if (flag) {
			return ResponseMessage.success();
		}
		return ResponseMessage.error("验证码发送失败");
	}

	@GetMapping("/code/{phone}/{code}")
	public ResponseMessage checkPhoneCode(@PathVariable String phone, @PathVariable String code) {
		if (userService.checkCode(phone, code)) {
			return ResponseMessage.success();
		} else {
			return ResponseMessage.error("验证码错误");
		}
	}

	@PutMapping("/info/check")
	public ResponseMessage verifyUserInfo(User user, String messageCode, HttpSession session) {
		Boolean flag = userService.checkCode(user.getPhone(), messageCode);
		if (!flag) {
			return ResponseMessage.error("验证码错误");
		}
		Boolean isOk = userService.Authentication(user);
		if (!isOk || Objects.isNull(user.getIdCard()) || Objects.isNull(user.getName())) {
			return ResponseMessage.error("认证失败,请检查您的有效身份信息");
		}
		User o = (User) session.getAttribute(KeyConstants.USER_SESSION);
		user.setId(o.getId());
		user = userService.modifyUserInfo(user);
		session.setAttribute(KeyConstants.USER_SESSION, user);
		return ResponseMessage.success();
	}


}
