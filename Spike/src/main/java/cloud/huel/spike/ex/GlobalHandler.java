package cloud.huel.spike.ex;

import cloud.huel.spike.pub.ResponseStatus;
import cloud.huel.spike.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 张晓华
 * @date 2022-9-3
 */
@Slf4j
@ControllerAdvice
public class GlobalHandler {


	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public ResultVO defaultHandler(Throwable throwable) {
		log.error("记录一次未知异常{}", throwable);
		return ResultVO.error(ResponseStatus.UNKNOWN);
	}

	@ExceptionHandler(BindException.class)
	@ResponseBody
	public ResultVO notValidExceptionHandler(BindException exception) {
		log.error("记录一次参数异常:{}", exception.getLocalizedMessage());
		BindingResult bindingResult = exception.getBindingResult();
		ResultVO resultVO = ResultVO.error(ResponseStatus.PARAMETER_EXCEPTION);
		bindingResult.getFieldErrors().forEach(e->{
			resultVO.addData(e.getField(), e.getDefaultMessage());
		});
		return resultVO;
	}


	@ExceptionHandler({SpikeException.class, OrderException.class})
	@ResponseBody
	public ResultVO orderExceptionHandler(Exception e) {
		log.error("重复请购异常", e.getLocalizedMessage());
		return ResultVO.error(e.getMessage());
	}




}
