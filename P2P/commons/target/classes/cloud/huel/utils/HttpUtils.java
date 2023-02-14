package cloud.huel.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


/**
 * @author 张晓华
 * @date 2022-7-16
 */
public final class HttpUtils {

	private HttpUtils() {
	}



	public static String get() throws Exception {
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		URIBuilder uriBuilder = new URIBuilder("https://home.firefoxchina.cn/");
		HttpGet get = new HttpGet(uriBuilder.build());
		CloseableHttpResponse response = closeableHttpClient.execute(get);
		HttpEntity entity = null;
		if (response != null && response.getStatusLine() != null) {
			if (response.getEntity() != null) {
				entity = response.getEntity();
			}
		}
		String content = EntityUtils.toString(entity, "UTF-8");
		closeableHttpClient.close();
		response.close();
		return content;
	}



}
