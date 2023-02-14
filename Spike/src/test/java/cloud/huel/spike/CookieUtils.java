package cloud.huel.spike;

import cloud.huel.spike.domain.User;
import cloud.huel.spike.util.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 张晓华
 * @date 2022-9-7
 */
@Slf4j
public class CookieUtils {

	private static volatile FileOutputStream fileOutputStream = null;

	public CookieUtils() throws FileNotFoundException {
	}

	public static void getCookie(String id, String password) throws IOException, JSONException {
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		JSONObject jsonObject = new JSONObject();
		ArrayList<NameValuePair> pairs = new ArrayList<>();
		BasicNameValuePair pair = new BasicNameValuePair("id", id);
		pairs.add(pair);
		pair = new BasicNameValuePair("password", password);
		pairs.add(pair);
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8);
		HttpPost request = new HttpPost("http://127.0.0.1:8080/user/login");
		System.out.println( "表单" + formEntity);
		request.setEntity(formEntity);
		CloseableHttpResponse httpResponse = httpClient.execute(request);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			System.out.println(EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8));
			List<Cookie> cookies = cookieStore.getCookies();
			cookies.forEach(c->{
				String name = c.getName();
				String value = c.getValue();
				if (name.equals("SESSION")) {
					String content = value + "\r\n";
					System.out.println("Cookie == " + content);
					try {
						fileOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
					} catch (IOException e) {
						log.error("出错了");
						throw new RuntimeException(e);
					}
				}

			});
		} else {
			log.error("请求失败");
		}
	}


	public static void generateUser() throws SQLException, ClassNotFoundException, IOException, JSONException {
		fileOutputStream = new FileOutputStream(new File("D:\\appsdata\\jar\\config.txt"));
		//Class.forName("com.mysql.cj.jdbc.Driver");
		//Connection connection = DriverManager.getConnection("jdbc:mysql://150.xxx.xx.xxx:3306/shop?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8", "xxxxx", "xxxx");
		//String sql = "insert into  t_user(id, nickname, password, salt, registry_date, last_login_time, login_count)\n" +
		//		"VALUES\n" +
		//		"    (? , ?, '1844b64585da4c92723aeaeee52e7ebf', 'abcabc', now(), now(), 0);";
		//PreparedStatement statement = connection.prepareStatement(sql);
		User user = null;
		ArrayList<User> users = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			//statement.setLong(1,  19900000000L+i);
			//statement.setString(2,  "1844b64585da4c92723aeaeee52e7ebf");
			//statement.addBatch();
			//statement.clearParameters();
			user = new User();
			user.setRegistryDate(null);
			user.setId(Long.valueOf((19900000000L+i) +""));
			user.setPassword("d3b1294a61a07da9b49b6e22b2cbd7f9");
			users.add(user);
		}
		int i = 0;
		for (User u : users) {
			getCookie(u.getId().toString(), u.getPassword());
		}
	}


	public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException, JSONException {
		generateUser();
	}


	@Test
	public void getPasswd() {
		String passwd = MD5Utils.getPlainPasswd("123456");
		System.out.println(passwd);
	}

}
