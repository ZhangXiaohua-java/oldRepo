package cloud.huel.spike;

import cloud.huel.spike.util.MD5Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class SpikeApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void generatePassword() {
		// d3b1294a61a07da9b49b6e22b2cbd7f9
		//System.out.println(MD5Utils.getPlainPasswd("123456"));
		// b7797cce01b4b131b433b6acf4add449
		//System.out.println(MD5Utils.getPlainPasswd("d3b1294a61a07da9b49b6e22b2cbd7f9"));
		System.out.println(MD5Utils.fromPassToDbPass("b7797cce01b4b131b433b6acf4add449", "abcabc"));


	}


}
