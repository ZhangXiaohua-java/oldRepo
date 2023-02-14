package cloud.huel.constant;

/**
 * @author 张晓华
 * @date 2022-7-15
 */
public final class LockConstants {

	private LockConstants() {
	}

	//查询用户总人数的锁标识
	public static final String USER_COUNT_LOCK = "user:count:lock";
	//查询总投资金额的锁标识
	public static final String BID_MONEY_LOCK = "bid:money:lock";
	//查询年化收益率的锁标识
	public static final String AVG_RATE_LOCK = "avg:rate:key";

	public static final Integer LOCK_TIME = 3;

	public static final Long SLEEP_TIME = 1500L;

}
