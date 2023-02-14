package cloud.huel.service.impl;

import cloud.huel.constant.KeyConstants;
import cloud.huel.utils.KeyUtils;
import cloud.huel.constant.LockConstants;
import cloud.huel.domain.load.LoanInfo;
import cloud.huel.mapper.LoanInfoMapper;
import cloud.huel.service.LoanInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 张晓华
 * @date 2022-7-15
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@DubboService(interfaceClass = LoanInfoService.class, version = "1.0", timeout = 3000)
public class LoanInfoServiceImpl implements LoanInfoService {

	@Resource
	private RedisTemplate<Object, Object> redisTemplate;

	@Resource
	private LoanInfoMapper loanInfoMapper;

	/**
	 * 查询投资年化收益率
	 *
	 * @return 年化收益率
	 */
	@Override
	public Double queryAverageRateYearly() throws InterruptedException {
		ValueOperations<Object, Object> valueOperations = redisTemplate.opsForValue();
		Double rate = (Double) valueOperations.get(KeyConstants.YEARLY_AVG_RATE);
		String uuid = null;
		if (Objects.isNull(rate)) {
			uuid = KeyUtils.uuid();
			Boolean flag = valueOperations.setIfAbsent(LockConstants.AVG_RATE_LOCK, uuid, Duration.ofSeconds(LockConstants.LOCK_TIME));
			if (flag) {
				if (!valueOperations.getOperations().hasKey(KeyConstants.YEARLY_AVG_RATE)) {
					rate = loanInfoMapper.selectAvgRate();
					log.info("从数据库中查询年化收益率 :" + rate);
					valueOperations.set(KeyConstants.YEARLY_AVG_RATE, rate, Duration.ofHours(LockConstants.LOCK_TIME));
					if (valueOperations.get(LockConstants.AVG_RATE_LOCK).equals(uuid)) {
						valueOperations.getOperations().delete(LockConstants.AVG_RATE_LOCK);
					}
				}
			} else {
				TimeUnit.MILLISECONDS.sleep(LockConstants.SLEEP_TIME);
				rate = (Double) valueOperations.get(KeyConstants.YEARLY_AVG_RATE);
			}
		} else {
			log.info("从Redis中直接获取到了年化收益率:" + rate);
		}
		return rate;
	}

	/**
	 * 查询指定条数的产品
	 *
	 * @param productType 三个值,0,1,2
	 * @param start       开始位置
	 * @param end         数据条数
	 * @return
	 */
	@Override
	public List<LoanInfo> queryLoanInfoByProductTypeForPage(String productType, Integer start, Integer end) {
		List<LoanInfo> loanInfos = loanInfoMapper.selectLoanInfoByProductType(productType, start, end);
		return loanInfos;
	}


	/**
	 * 查询表中记录数
	 *
	 * @return 总记录数
	 */
	@Override
	public Long queryTotalRows(String productType) {
		if (productType == null) {
			return loanInfoMapper.getRowsCount();
		} else {
			return loanInfoMapper.getRowsCountByType(productType);
		}
	}

	/**
	 * 查询产品的详细信息
	 *
	 * @param id 产品id
	 * @return 产品信息
	 */
	@Override
	public LoanInfo queryLoanDetailInfo(Integer id) {
		LoanInfo loanInfo = loanInfoMapper.selectDetailLoanInfo(id);
		return loanInfo;
	}


}
