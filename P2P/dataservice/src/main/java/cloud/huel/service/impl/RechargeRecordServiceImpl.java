package cloud.huel.service.impl;

import cloud.huel.domain.load.RechargeRecord;
import cloud.huel.mapper.RechargeRecordMapper;
import cloud.huel.service.RechargeRecordService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 张晓华
 * @date 2022-7-20
 */
@Transactional(rollbackFor = {Exception.class})
@DubboService(version = "1.0", timeout = 2000)
public class RechargeRecordServiceImpl implements RechargeRecordService {

	@Resource
	private RechargeRecordMapper rechargeRecordMapper;

	@Override
	public Boolean recharge(Integer uid, Integer chargeMoney, String rechargeNo) {
		RechargeRecord record = new RechargeRecord();
		record.setRechargeNo(rechargeNo);
		record.setRechargeMoney(Double.valueOf(chargeMoney) * 100);
		record.setUid(uid);
		//0 代表充值中
		record.setRechargeStatus("0");
		record.setRechargeTime(new Date());
		record.setRechargeDesc("购买理财产品充值");
		Integer rowCount = rechargeRecordMapper.insert(record);
		if (rowCount == null || rowCount == 0) {
			return false;
		}
		return true;
	}


}
