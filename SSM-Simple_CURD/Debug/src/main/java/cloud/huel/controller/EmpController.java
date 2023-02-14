package cloud.huel.controller;

import cloud.huel.domain.Emp;
import cloud.huel.domain.Message;
import cloud.huel.service.EmpService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author 张晓华
 * @version 1.0
 */
@Controller
public class EmpController {
	@Autowired
	private EmpService service;
	private Integer pageSize = 10;

	/**
	 * jsp的分页查询
	 * @param pageNum
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/emp",method = RequestMethod.GET)
	public String pageEmp(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
						  Model model){
		PageInfo<Emp> info = service.queryEmpByPage(pageNum, pageSize);
		model.addAttribute("info",info);
		System.out.println(info);
		return "emp";
	}

	/**
	 * 响应数据格式为JSON的分页查询
	 * @param pageNum
	 * @return
	 */
	@GetMapping("/emps")
	@ResponseBody
	public Message processPageQueryWithBean(
			@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum){
		PageInfo<Emp> page = service.queryEmpByPage(pageNum, pageSize);
		return Message.processSuccessRequest().add("page",page);
	}



	@RequestMapping(value = "/toEmp",method = RequestMethod.GET)
	public String toEmps(){
		return "emps";
	}

	/**
	 * 添加新员工
	 * @param emp
	 * @param result
	 * @return
	 */
	@PostMapping("/emp")
	@ResponseBody
	public Message addEmp(@Valid Emp emp, BindingResult result){
		if (!result.hasErrors()) {
			Integer row = service.addEmp(emp);
			if (row == 1) {
				return Message.processSuccessRequest();

			}

		}
		Map<String,Object> errorMap = new HashMap<>();
			List<FieldError> fieldErrors = result.getFieldErrors();
//			fieldErrors.forEach(error->{
//				errorMap.put(error.getField(),error.getDefaultMessage());
//			});
//		errorMap.forEach((key,value)->{
//			System.out.println("错误字段"+key);
//			System.out.println("错误字段"+value);
//		});
		for (int i = 0; i < fieldErrors.size(); i++) {
			String field = fieldErrors.get(i).getField();
			String message = fieldErrors.get(i).getDefaultMessage();
			System.out.println("错误字段"+field);
			System.out.println("错误提示信息"+message);
			errorMap.put(field,message);
		}
			return Message.processFailedRequest().add("responseMessage",errorMap);


	}

	/**
	 * 检查用户名是否已经存在
	 * @param empName
	 * @return
	 */
	@GetMapping("/empNameQuery")
	@ResponseBody
	public Message checkEmpIsDistinct(@RequestParam("empName")
												  String empName){
		boolean status = service.queryEmpNameIsExists(empName);
		if (status) {
			return Message.processSuccessRequest();
		}
		return  Message.processFailedRequest();
	}

	@GetMapping("/emp/{id}")
	@ResponseBody
	public Message queryEmpById(@PathVariable("id") Integer id){
		Objects.requireNonNull(id,"传入的参数不允许为空");
		Emp emp = service.queryEmpById(id);
		if (emp != null) {
			return Message.processSuccessRequest().add("emp",emp);
		}
		return  Message.processFailedRequest();
	}

	@PutMapping("/emp")
	@ResponseBody
	public Message updateEmpInfo(@Valid Emp emp,BindingResult result){
		Objects.requireNonNull(emp,"传入的参数不允许为空");
		if (!result.hasErrors()) {
			Integer row = service.updateEmpInfo(emp);
			return  Message.processSuccessRequest();
		}
		Map<String,Object> errorMap = new HashMap<>();
		List<FieldError> fieldErrors = result.getFieldErrors();
		fieldErrors.forEach((error)->{
			System.out.println("校验出错误的字段 "+error.getField());
			System.out.println("字段错误的原因 "+error.getDefaultMessage());
			errorMap.put(error.getField(),error.getDefaultMessage());
		});
		return  Message.processFailedRequest().add("error",errorMap);

	}

	@DeleteMapping("/emp")
	@ResponseBody
	public Message deleteEmp(String ids){

		if (ids == null) {
			return Message.processFailedRequest();
		}
		String[] strings = ids.split("-");
		Integer [] idsArray = new Integer[strings.length];
		for (int i = 0; i <strings.length ; i++) {
			if (strings[i] == null || "".equals(strings[i]) ) {
				return Message.processFailedRequest();
			}
			idsArray[i] = Integer.parseInt(strings[i]);
		}
		Integer result = service.batchDeleteEmp(idsArray);
		if (result != null && result >0) {
			return  Message.processSuccessRequest();
		}
		throw new RuntimeException("惊天大Bug");

	}



}
