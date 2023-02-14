package cloud.huel.spike.pub;

/**
 * @author 张晓华
 * @date 2022-9-3
 */
public enum ResponseStatus {



	SUCCESS(200L, "OK"),
	ERROR(500L, "error"),
	UNKNOWN(50000L, "系统忙,请稍候重试..."),
	NONE_PERMISSION(40001L, "无操作权限"),
	PARAMETER_EXCEPTION(50001L, "请检查手机号或者密码输入是否有误"),
	SPIKE_EXCEPTION(50002L,"活动火爆进行中,请稍候再试"),
	REPEATED_SPIKE(50003L, "秒杀活动每一位用户只能抢购一次"),
	NOT_EXIST_ORDER(50005L,"订单不存在"),
	SOLD_OUT(50006L, "活动过于火爆,商品已经被秒杀完了..."),
	WAITING(201, "排队中"),
	ILLEGAL(402, "非法请求"),
	CODE_NOT_MATCH(406, "验证码不匹配"),
	FREQUENT_OPERATION(407, "频繁操作,请稍候再试")

	;


	ResponseStatus(long code, String message) {
		this.code = code;
		this.message = message;
	}

	private long code;

	private String message;

	public long getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
