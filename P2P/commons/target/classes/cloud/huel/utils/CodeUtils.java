package cloud.huel.utils;

import java.util.Random;

/**
 * @author 张晓华
 * @date 2022-7-16
 */
public final class CodeUtils {

	private static Random random = new Random();

	private CodeUtils() {
	}


	public static String generateCode() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			sb.append(random.nextInt(10));
		}
		return sb.toString();
	}




}
