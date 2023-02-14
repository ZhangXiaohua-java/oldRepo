package cloud.huel.spike.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张晓华
 * @date 2022-9-3
 */
@Controller
public class DemoController {


	@GetMapping("/init")
	public String init() {
		return "hello";
	}


}
