package cloud.huel.service.impl;

import cloud.huel.constant.KeyConstants;
import cloud.huel.domain.load.LoanInfo;
import cloud.huel.domain.user.FinanceAccount;
import cloud.huel.domain.user.User;
import cloud.huel.exception.TradeFailException;
import cloud.huel.mapper.FinanceAccountMapper;
import cloud.huel.mapper.LoanInfoMapper;
import cloud.huel.mapper.UserMapper;
import cloud.huel.utils.KeyUtils;
import cloud.huel.constant.LockConstants;
import cloud.huel.domain.load.BidInfo;
import cloud.huel.mapper.BidInfoMapper;
import cloud.huel.service.BidInfoService;
import cloud.huel.vo.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 张晓华
 * @date 2022-7-15
 */
@Transactional(rollbackFor = Exception.class)
@Slf4j
@DubboService(interfaceClass = BidInfoService.class, version = "1.0", timeout = 2000)
public class BidInfoServiceImpl implements BidInfoService {

	@Autowired
	private BidInfoMapper bidInfoMapper;

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private FinanceAccountMapper financeAccountMapper;

	@Autowired
	private LoanInfoMapper loanInfoMapper;

	/**
	 * 查询平台所有用户历史投资金额
	 *
	 * @return 总投资金额
	 */
	@Override
	public Double queryHistoryTotalBidMoney() throws InterruptedException {
		ValueOperations<Object, Object> valueOperations = redisTemplate.opsForValue();
		Double totalBidMoney = (Double) valueOperations.get(KeyConstants.TOTAL_BID_MONEY_KEY);
		String uuid = null;
		if (Objects.isNull(totalBidMoney)) {
			uuid = KeyUtils.uuid();
			if (valueOperations.setIfAbsent(LockConstants.BID_MONEY_LOCK, uuid, Duration.ofSeconds(LockConstants.LOCK_TIME))) {
				totalBidMoney = bidInfoMapper.selectTotalBidMoney();
				log.info("从数据库中查询历史总投资金额: " + totalBidMoney);
				valueOperations.set(KeyConstants.TOTAL_BID_MONEY_KEY, totalBidMoney, Duration.ofHours(LockConstants.LOCK_TIME));
				if (valueOperations.get(LockConstants.BID_MONEY_LOCK).equals(uuid)) {
					valueOperations.getOperations().delete(LockConstants.BID_MONEY_LOCK);
				}
			} else {
				TimeUnit.MILLISECONDS.sleep(LockConstants.SLEEP_TIME);
				totalBidMoney = (Double) valueOperations.get(KeyConstants.TOTAL_BID_MONEY_KEY);
			}
		} else {
			log.info("从Redis中查询历史总投资金额: " + totalBidMoney);
		}
		return totalBidMoney;
	}


	/**
	 * 查询指定产品的最近的投资历史记录
	 *
	 * @param productId
	 * @return 历史投资记录
	 */
	@Override
	public List<BidInfo> queryInvestHistory(Integer productId) {
		List<BidInfo> tradeHistory = bidInfoMapper.selectTradeHistory(productId);
		return tradeHistory;
	}

	/**
	 * 逻辑流程:
	 * 1 记录交易信息
	 * 2 用户资金账户余额扣除
	 * 3 产品筹措资金减去本次融资金额
	 * 4 如果产品的融资金额等于0,就将产品的状态置为1
	 * 5 向Redis中假如或增加投资记录排名信息
	 * 用户投资
	 *
	 * @param productId 产品ID
	 * @param bidMoney  交易金额
	 * @param user
	 * @return user 交易后的信息
	 * @throws TradeFailException 可能的异常
	 */
	@Transactional(rollbackFor = {Exception.class, TradeFailException.class})
	@Override
	public User invest(Integer productId, Long bidMoney, User user) throws TradeFailException {
		//新增交易信息
		BidInfo bidInfo = new BidInfo();
		bidInfo.setBidStatus(1);
		bidInfo.setBidTime(new Date());
		bidInfo.setUid(user.getId());
		bidInfo.setLoanId(productId);
		bidInfo.setBidMoney(Double.valueOf(bidMoney));
		Integer rowCount = bidInfoMapper.insert(bidInfo);
		if (rowCount == null || rowCount == 0) {
			throw new TradeFailException("交易失败");
		}
		//用户余额扣除
		FinanceAccount account = financeAccountMapper.selectFinanceAccountByUid(user.getId());
		Double money = account.getAvailableMoney();
		money -= bidMoney;
		account.setAvailableMoney(money);
		rowCount = financeAccountMapper.updateUserAccount(account);
		if (rowCount == null || rowCount == 0) {
			throw new TradeFailException("交易失败");
		}
		//产品剩余筹措资金扣除
		LoanInfo loanInfo = loanInfoMapper.selectByPrimaryKey(productId);
		money = loanInfo.getLeftProductMoney() - bidMoney;
		if (money < 0) {
			throw new TradeFailException("交易失败");
		}
		loanInfo.setLeftProductMoney(money);
		if (money == 0) {
			loanInfo.setProductStatus(1);
		}
		log.info("更新产品信息 = " + loanInfo);
		rowCount = loanInfoMapper.updateLoanInfo(loanInfo);
		if (rowCount == null || rowCount == 0) {
			throw new TradeFailException("交易失败");
		}
		redisTemplate.opsForZSet().incrementScore(KeyConstants.TRADE_TOP, user.getPhone(), Double.valueOf(bidMoney));
		user.setFinanceAccount(account);
		return user;
	}


	/**
	 * 查询投资排名信息
	 *
	 * @return
	 */
	@Override
	public List<Tuple> queryRank() {
		ZSetOperations<Object, Object> operations = redisTemplate.opsForZSet();
		Set<ZSetOperations.TypedTuple<Object>> tupleSet = operations.reverseRangeWithScores(KeyConstants.TRADE_TOP, 0, 6);
		ArrayList<Tuple> list = new ArrayList<>();
		tupleSet.forEach(e -> {
			Tuple tuple = new Tuple();
			tuple.setScore(e.getScore());
			tuple.setValue(e.getValue());
			list.add(tuple);
		});
		return list;
	}

}
