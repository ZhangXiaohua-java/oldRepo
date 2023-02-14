package cloud.huel.mapper;

import cloud.huel.domain.load.BidInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BidInfoMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(BidInfo record);

	int insertSelective(BidInfo record);

	BidInfo selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(BidInfo record);

	int updateByPrimaryKey(BidInfo record);

	/**
	 * 查询平台历史总投资金额
	 *
	 * @return 平台历史总投资金额
	 */
	@Select("select sum(bid_money) from b_bid_info")
	Double selectTotalBidMoney();


	/**
	 * 查询指定的产品历史交易记录
	 *
	 * @param productId 产品id
	 * @return 历史交易记录
	 */
	@Select("select b.bid_money as money,u.phone as phone,b.bid_time as time from b_bid_info as b inner join u_user as u on b.uid = u.id where b.loan_id = #{id}  order by b.bid_time desc limit 0,9")
	@Results({
			@Result(column = "money", property = "bidMoney"),
			@Result(column = "phone", property = "user.phone"),
			@Result(column = "time", property = "bidTime")
	})
	List<BidInfo> selectTradeHistory(@Param("id") Integer productId);

	/**
	 * 查询指定产品的所有投资记录
	 *
	 * @return
	 */
	List<BidInfo> selectAllRecordsByLoanId(@Param("loanId") Integer loanId);


}