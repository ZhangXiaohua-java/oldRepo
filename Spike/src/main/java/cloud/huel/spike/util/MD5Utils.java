package cloud.huel.spike.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author 张晓华
 * @date 2022-9-2
 */
public final class MD5Utils {

	private static final String SALT = "1a2b3c4d";

	// 基础加密
	public static String md5(String input) {
		return DigestUtils.md5Hex(input);
	}

	// 第一次加密,混淆密码
	public static String getPlainPasswd(String input) {
		input = "" + SALT.charAt(0) + SALT.charAt(2) + input +SALT.charAt(5) + SALT.charAt(4);
		return md5(input);
	}

	// 第二次加密,根据指定的盐再次混淆密码,这个salt盐是要存入数据库的, 用户注册时使用.
	public static String fromPassToDbPass(String input, String salt) {
		input = "" + salt.charAt(0) + salt.charAt(2) + input + salt.charAt(5) +salt.charAt(4);
		return md5(input);
	}

	// 这个方法是对上面两个方法的一个封装
	public static String inputPassToDbPass(String input, String salt) {
		input = getPlainPasswd(input);
		return fromPassToDbPass(input, salt);
	}






}
