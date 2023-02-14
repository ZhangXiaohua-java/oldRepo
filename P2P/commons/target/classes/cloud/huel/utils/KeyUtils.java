package cloud.huel.utils;

import java.util.UUID;

/**
 * @author 张晓华
 * @date 2022-7-15
 */
public final class KeyUtils {

	private KeyUtils() {
	}

	/**
	 * @return 随机生成的uuid
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}


}
