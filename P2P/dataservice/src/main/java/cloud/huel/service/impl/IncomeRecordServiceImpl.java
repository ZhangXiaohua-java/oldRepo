package cloud.huel.service.impl;

import cloud.huel.domain.load.BidInfo;
import cloud.huel.domain.load.IncomeRecord;
import cloud.huel.domain.load.LoanInfo;
import cloud.huel.domain.user.FinanceAccount;
import cloud.huel.exception.IncomeCountException;
import cloud.huel.mapper.BidInfoMapper;
import cloud.huel.mapper.FinanceAccountMapper;
import cloud.huel.mapper.IncomeRecordMapper;
import cloud.huel.mapper.LoanInfoMapper;
import cloud.huel.service.IncomeRecordService;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author 张晓华
 * @date 2022-7-19
 */
@Transactional(rollbackFor = {Exception.class, IncomeCountException.class})
@DubboService(version = "1.0", timeout = 10000, retries = 3)
public class IncomeRecordServiceImpl implements IncomeRecordService {

	@Autowired
	private BidInfoMapper bidInfoMapper;

	@Autowired
	private IncomeRecordMapper incomeRecordMapper;

	@Autowired
	private LoanInfoMapper loanInfoMapper;

	@Autowired
	private FinanceAccountMapper financeAccountMapper;

	/**
	 * 执行生成预期收益计划
	 * 1 查询所有资金筹措完毕的产品
	 * 2 查询该产品的所有投资记录,将投资的状态置为2
	 * 3 生成收益记录,状态置为1
	 * 4
	 *
	 * @return 执行结果
	 */
	@Override
	public Boolean generateExceptedIncome() throws IncomeCountException {
		IncomeRecord record = null;
		Double interest = null;
		Double rate = null;
		Date date = null;
		List<LoanInfo> loanInfos = loanInfoMapper.selectAllSoldOutLoans();
		for (LoanInfo loanInfo : loanInfos) {
			List<BidInfo> bidInfos = bidInfoMapper.selectAllRecordsByLoanId(loanInfo.getId());
			for (BidInfo bidInfo : bidInfos) {
				record = new IncomeRecord();
				record.setBidMoney(bidInfo.getBidMoney());
				record.setIncomeStatus(1);
				record.setLoanId(bidInfo.getLoanId());
				record.setUid(bidInfo.getUid());
				record.setBidId(bidInfo.getId());
				rate = (loanInfo.getRate() / 100) / 365;
				if (loanInfo.getProductType() == 0) {
					interest = rate * bidInfo.getBidMoney() * loanInfo.getCycle();
					//保留三个小数位
					interest = new BigDecimal(interest).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
					date = DateUtils.addDays(new Date(), loanInfo.getCycle());
					record.setIncomeMoney(interest);
					record.setIncomeDate(date);
				} else {
					interest = rate * bidInfo.getBidMoney() * loanInfo.getCycle() * 30;
					//保留三个小数位
					interest = new BigDecimal(interest).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
					date = DateUtils.addMonths(new Date(), loanInfo.getCycle());
					record.setIncomeMoney(interest);
					record.setIncomeDate(date);
				}
				Integer rowCount = incomeRecordMapper.insert(record);
				if (rowCount == null || rowCount == 0) {
					throw new IncomeCountException("新增收益记录出错");
				}
			}
			LoanInfo info = new LoanInfo();
			info.setProductStatus(2);
			info.setId(loanInfo.getId());
			Integer count = loanInfoMapper.updateByPrimaryKeySelective(info);
			if (count == null || count == 0) {
				throw new IncomeCountException("新增收益记录出错");
			}
		}

		return true;

	}


	/**
	 * 返还用户的本金和收益
	 * 1 查询所有收益记录表中状态为1的所有记录
	 * 2 计算返还收益和利息之后用户的余额,并且将记录全部状态全部置为2
	 *
	 * @return 执行结果
	 */
	@Override
	public Boolean returnIncome() throws IncomeCountException {
		Double money = null;
		FinanceAccount financeAccount = null;
		List<IncomeRecord> incomeRecords = incomeRecordMapper.selectAllRecordTobeReturn();
		for (IncomeRecord incomeRecord : incomeRecords) {
			//计算返还本金和利息之后的用户账户余额
			money = incomeRecord.getIncomeMoney();
			money += incomeRecord.getBidMoney();
			Integer uid = incomeRecord.getUid();
			financeAccount = financeAccountMapper.selectFinanceAccountByUid(uid);
			if (financeAccount.getAvailableMoney() != null) {
				money += financeAccount.getAvailableMoney();
			}
			financeAccount.setAvailableMoney(money);
			Integer rowCount = financeAccountMapper.updateByPrimaryKeySelective(financeAccount);
			if (rowCount == null || rowCount == 0) {
				throw new IncomeCountException("收益分配失败");
			}
			IncomeRecord record = new IncomeRecord();
			record.setId(incomeRecord.getId());
			record.setIncomeStatus(2);
			rowCount = incomeRecordMapper.updateByPrimaryKeySelective(record);
			if (rowCount == null || rowCount == 0) {
				throw new IncomeCountException("收益分配失败");
			}
		}

		return true;
	}
}
