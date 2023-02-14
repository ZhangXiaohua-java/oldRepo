package cloud.huel.mapper;

import cloud.huel.domain.load.LoanInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface LoanInfoMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(LoanInfo record);

	int insertSelective(LoanInfo record);

	LoanInfo selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(LoanInfo record);

	int updateByPrimaryKey(LoanInfo record);

	/**
	 * 查询平台年化收益率
	 *
	 * @return 平台年化收益率, 保留三位小数位
	 */
	@Select("select cast(avg(rate) as decimal (10,3)) from b_loan_info")
	Double selectAvgRate();


	List<LoanInfo> selectLoanInfoByProductType(@Param("type") String productType,
											   @Param("start") Integer start, @Param("end") Integer end);

	/**
	 * 获取表中的记录数
	 *
	 * @return
	 */
	@Select("select count(*) from p2p.b_loan_info")
	Long getRowsCount();

	/**
	 * 查询指定类型的产品的数量
	 *
	 * @param productType 产品类型
	 * @return 产品数量
	 */
	@Select("select count(*) from p2p.b_loan_info where product_type = #{type}")
	Long getRowsCountByType(@Param("type") String productType);

	LoanInfo selectDetailLoanInfo(@Param("id") Integer id);

	/**
	 * 更新用户购买产品的信息
	 *
	 * @param loanInfo
	 * @return
	 */
	Integer updateLoanInfo(LoanInfo loanInfo);

	/**
	 * 查询所有待生成收益记录的产品信息(资金全部筹措完毕)
	 *
	 * @return
	 */
	List<LoanInfo> selectAllSoldOutLoans();


}