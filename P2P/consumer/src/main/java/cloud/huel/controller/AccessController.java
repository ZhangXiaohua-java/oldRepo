package cloud.huel.controller;

import cloud.huel.constant.KeyConstants;
import cloud.huel.domain.user.User;
import cloud.huel.service.RechargeRecordService;
import cloud.huel.service.UserService;
import cloud.huel.utils.DateUtils;
import cloud.huel.utils.ProcessParameter;
import cloud.huel.vo.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Objects;

/**
 * @author 张晓华
 * @date 2022-7-17
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class AccessController {

	@DubboReference(interfaceClass = UserService.class, version = "1.0", timeout = 2000, retries = 3)
	private UserService userService;

	@DubboReference(version = "1.0", timeout = 2000, retries = 3)
	private RechargeRecordService rechargeRecordService;


	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute(KeyConstants.USER_SESSION);
		session.invalidate();
		return "redirect:/";
	}

	@PostMapping("/login")
	@ResponseBody
	public ResponseMessage login(User user, @RequestParam("messageCode") String code, HttpSession session) {
		if (Objects.isNull(user.getPhone()) || Objects.isNull(user.getLoginPassword())
				|| Objects.isNull(code)) {
			return ResponseMessage.error("请检查登录信息是否有误");
		}
		if (!userService.checkCode(user.getPhone(), code)) {
			return ResponseMessage.error("请检查输入的验证码");
		}
		user = userService.loginCheck(user);
		if (Objects.isNull(user)) {
			return ResponseMessage.error("请检查账号或者密码是否有误");
		}
		session.setAttribute(KeyConstants.USER_SESSION, user);
		log.info("用户 " + user.getName() + "登录,手机号为: " + user.getPhone());
		return ResponseMessage.success();
	}

	@GetMapping("/loanCenter")
	public String toLoanCenter(HttpSession httpSession) {
		User user = (User) httpSession.getAttribute(KeyConstants.USER_SESSION);
		return "center";
	}

	//registry.addViewController("/user/recharge").setViewName("charge");
	@GetMapping(value = "/recharge", params = "money")
	public String charge(Integer money, HttpSession session, ModelMap modelMap) throws UnsupportedEncodingException {
		User user = (User) session.getAttribute(KeyConstants.USER_SESSION);
		String phone = user.getPhone();
		Map<String, Object> map = ProcessParameter.getParameter(phone, DateUtils.getRecordId(), money);
		modelMap.addAllAttributes(map);
		Boolean flag = rechargeRecordService.recharge(user.getId(), money, String.valueOf(map.get("orderId")));
		if (!flag) {
			return "error";
		}
		log.info(" Map" + map);
		return "charge";
	}


}
