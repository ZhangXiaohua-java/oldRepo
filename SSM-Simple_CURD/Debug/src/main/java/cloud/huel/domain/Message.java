package cloud.huel.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 张晓华
 * @version 1.0
 */
public class Message implements Serializable {
	private Integer code;
	private String message;
	private Map<String,Object> map = new HashMap<>();

	public Message() {
	}

	public Message(Integer code, String message, Map<String, Object> map) {
		this.code = code;
		this.message = message;
		this.map = map;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	public static Message processSuccessRequest(){
		Message message = new Message();
		message.setMessage("请求处理成功");
		message.setCode(200);
		return message;
	}
	public static Message processFailedRequest(){
		Message message = new Message();
		message.setCode(500);
		message.setMessage("请求处理失败,请检查提交的参数");
		return  message;
	}
	public  Message add(String key,Object value){
		 this.getMap().put(key,value);
		 return this;
	}


}
