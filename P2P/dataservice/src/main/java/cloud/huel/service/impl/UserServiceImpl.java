package cloud.huel.service.impl;

import cloud.huel.constant.KeyConstants;
import cloud.huel.utils.CodeUtils;
import cloud.huel.utils.KeyUtils;
import cloud.huel.constant.LockConstants;
import cloud.huel.domain.user.FinanceAccount;
import cloud.huel.domain.user.User;
import cloud.huel.mapper.FinanceAccountMapper;
import cloud.huel.mapper.UserMapper;
import cloud.huel.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * @author 张晓华
 * @date 2022-7-14
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
@Slf4j
@DubboService(interfaceClass = UserService.class, version = "1.0", timeout = 3000)
public class UserServiceImpl implements UserService {

	@Resource
	private UserMapper userMapper;
	@Resource
	private RedisTemplate<Object, Object> redisTemplate;

	@Autowired
	private FinanceAccountMapper financeAccountMapper;


	/**
	 * 查询平台注册总人数
	 *
	 * @return 平台注册总人数
	 */
	@Override
	public Long queryUserCount() throws InterruptedException {
		ValueOperations<Object, Object> valueOperations = redisTemplate.opsForValue();
		Long count = (Long) valueOperations.get(KeyConstants.TOTAL_USER_KEY);
		String uuid = null;
		if (Objects.isNull(count)) {
			uuid = KeyUtils.uuid();
			Boolean flag = valueOperations.setIfAbsent(LockConstants.USER_COUNT_LOCK, uuid, Duration.ofSeconds(LockConstants.LOCK_TIME));
			if (flag) {
				Boolean exists = valueOperations.getOperations().hasKey(KeyConstants.TOTAL_USER_KEY);
				if (!exists) {

					count = userMapper.selectTotalUserCount();
					log.info("从数据库中查询总人数数据:" + count);
					valueOperations.set(KeyConstants.TOTAL_USER_KEY, count, Duration.ofHours(LockConstants.LOCK_TIME));
					if (valueOperations.get(LockConstants.USER_COUNT_LOCK).equals(uuid)) {
						valueOperations.getOperations().delete(LockConstants.USER_COUNT_LOCK);
					}
				}
			} else {
				TimeUnit.MILLISECONDS.sleep(LockConstants.SLEEP_TIME);
				count = (Long) valueOperations.get(KeyConstants.TOTAL_USER_KEY);
			}
		} else {
			log.info("从Redis中查到了总人数数据" + count);
		}
		return count;
	}


	/**
	 * 用户注册
	 *
	 * @param user 手机号与密码
	 * @return 用户信息
	 */
	@Override
	public User register(User user) {
		user.setAddTime(new Date());
		user.setLastLoginTime(new Date());
		Integer rowNumber = userMapper.insertSelective(user);
		if (rowNumber == null || rowNumber == 0) {
			return null;
		}
		Objects.requireNonNull(user.getId(), "用户ID不能为空");
		FinanceAccount account = new FinanceAccount();
		account.setUid(user.getId());
		account.setAvailableMoney(888D);
		Integer count = financeAccountMapper.insertSelective(account);
		if (count == null || count == 0) {
			return null;
		}
		user.setFinanceAccount(account);
		log.info("用户信息:" + user);
		return user;
	}


	/**
	 * 检查手机号是否已经被注册
	 *
	 * @param phone 手机号
	 * @return 布尔
	 */
	@Override
	public Boolean phoneExists(String phone) {
		Integer count = userMapper.phoneExists(phone);
		if (count > 0) {
			return false;
		}
		return true;
	}


	/**
	 * 发送验证码
	 *
	 * @param phone 手机号
	 * @return 成功与否的标志
	 */
	@Override
	public Boolean sendCode(String phone) {
		String code = CodeUtils.generateCode();
		log.info("本次生成的验证码是:" + code);
		//请求第三方验证码暂时搁置以下
		ValueOperations<Object, Object> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(KeyConstants.PHONE_CODE_PREFIX + phone, code, Duration.ofMinutes(LockConstants.LOCK_TIME));
		return true;
	}

	/**
	 * 校验输入的验证码是否正确
	 *
	 * @param phone 手机号
	 * @param code  验证码
	 * @return 是否匹配
	 */
	@Override
	public Boolean checkCode(String phone, String code) {
		if (phone == null || code == null) {
			return false;
		}
		ValueOperations<Object, Object> valueOperations = redisTemplate.opsForValue();
		String result = (String) valueOperations.get(KeyConstants.PHONE_CODE_PREFIX + phone);
		if (Objects.isNull(result)) {
			return false;
		}
		if (!Objects.equals(result, code)) {
			return false;
		}
		return true;
	}

	/**
	 * 用户实名认证
	 *
	 * @param user 用户信息
	 * @return 认证结果
	 */
	@Override
	public Boolean Authentication(User user) {
		//待写
		return true;
	}

	/**
	 * 修改用户信息
	 *
	 * @param user 用户信息
	 * @return 修改后的用户信息
	 */
	@Override
	public User modifyUserInfo(User user) {
		Integer rowCount = userMapper.updateByPrimaryKeySelective(user);
		log.info("更新用户信息: 参数 ==" + user);
		if (rowCount == null || rowCount == 0) {
			return null;
		}
		User u = userMapper.selectByPrimaryKey(user.getId());
		FinanceAccount financeAccount = financeAccountMapper.selectFinanceAccountByUid(user.getId());
		u.setFinanceAccount(financeAccount);
		return u;
	}

	/**
	 * 用户登录检查
	 *
	 * @param user 手机号与密码信息
	 * @return 完整的用户信息及其账户信息
	 */
	@Override
	public User loginCheck(User user) {
		user = userMapper.selectUserByPhoneAndPassword(user);
		if (Objects.isNull(user)) {
			return null;
		}
		FinanceAccount financeAccount = financeAccountMapper.selectFinanceAccountByUid(user.getId());
		user.setFinanceAccount(financeAccount);
		return user;
	}


}
