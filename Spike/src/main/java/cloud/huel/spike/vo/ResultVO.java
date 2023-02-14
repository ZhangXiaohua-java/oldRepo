package cloud.huel.spike.vo;

import cloud.huel.spike.pub.ResponseStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 张晓华
 * @date 2022-9-3
 */

public class ResultVO implements Serializable {

	private static final long serialVersionUID = 7072969420545603307L;


	private long code;

	private String message;

	private Map<String, Object> resultMap;


	// 此构造方法只允许在当前类中调用
	private ResultVO(long code, String message) {
		this.code = code;
		this.message = message;
		this.resultMap = new HashMap<>();
	}

	// 公共权限的构造方法, 旨在给客户端响应时可以根据API快速判断响应的结果类型,通过枚举对象就可以见名知意.
	public ResultVO(ResponseStatus status) {
		this(status.getCode(), status.getMessage());
	}


	// 成功
	public static ResultVO success(ResponseStatus status) {
		return new ResultVO(status.getCode(), status.getMessage());
	}

	// 失败
	public static ResultVO error(ResponseStatus status) {
		return new ResultVO(status.getCode(), status.getMessage());
	}

	public static ResultVO error(String message) {
		return new ResultVO(500, message);
	}

	// 向已经初始化过的Map中添加响应数据
	public ResultVO addData(String key, Object value) {
		this.resultMap.put(key, value);
		return this;
	}

	public long getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

}
