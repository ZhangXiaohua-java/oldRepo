package cloud.huel.mapper;

import cloud.huel.domain.user.FinanceAccount;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface FinanceAccountMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(FinanceAccount record);

	int insertSelective(FinanceAccount record);

	FinanceAccount selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(FinanceAccount record);

	int updateByPrimaryKey(FinanceAccount record);

	FinanceAccount selectFinanceAccountByUid(@Param("uid") Integer uid);

	/**
	 * 更新用户交易完之后的账户余额
	 *
	 * @param account 受影响行数
	 * @return
	 */
	@Update("update u_finance_account set available_money = #{availableMoney} where uid = uid and available_money > 0")
	Integer updateUserAccount(FinanceAccount account);


}