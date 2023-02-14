package cloud.huel.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 张晓华
 * @date 2022-7-19
 */
public final class DateUtils {

	private static AtomicInteger atomicInteger = new AtomicInteger();

	private DateUtils() {
	}

	public static String getRecordId() {
		int order = atomicInteger.addAndGet(1);
		return DateFormatUtils.format(new Date(), "yyyyMMddHHmmsss") + order;
	}

	public static String getDate () {
		return DateFormatUtils.format(new Date(), "yyyyMMddHHmmsss");
	}

}
