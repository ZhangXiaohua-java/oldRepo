package cloud.huel.spike.controller;

import cloud.huel.spike.constant.UserConstants;
import cloud.huel.spike.domain.User;
import com.wf.captcha.ChineseGifCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author 张晓华
 * @date 2022-9-9
 */
@Slf4j
@RequestMapping("/verify")
@Controller
public class VerifyController {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@GetMapping("/captcha")
	public void generateCode(HttpSession session,
							 Integer gid,
							 HttpServletResponse response) throws IOException {
		User user = (User) session.getAttribute(UserConstants.USER_KEY);
		response.setContentType("image/jpeg");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		ChineseGifCaptcha captcha = new ChineseGifCaptcha(130, 50, 6);
		String text = captcha.text();
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		ops.set(UserConstants.CODE_PREFIX + user.getId().intValue() + ":" + gid, text);
		captcha.out(response.getOutputStream());
	}



}
