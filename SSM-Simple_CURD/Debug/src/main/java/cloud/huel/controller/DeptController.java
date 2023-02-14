package cloud.huel.controller;

import cloud.huel.domain.Dept;
import cloud.huel.domain.Message;
import cloud.huel.service.DeptService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 张晓华
 * @version 1.0
 */
@RestController
public class DeptController {
	@Resource
	DeptService deptService;
	@RequestMapping("/dept")
	public Message queryAllDept(){
		List<Dept> deptList = deptService.queryAllDept();
		return Message.processSuccessRequest().add("dept",deptList);

	}
}
