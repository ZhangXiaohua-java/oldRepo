package cloud.huel.mapper;

import cloud.huel.domain.load.IncomeRecord;

import java.util.List;

public interface IncomeRecordMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(IncomeRecord record);

	int insertSelective(IncomeRecord record);

	IncomeRecord selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(IncomeRecord record);

	int updateByPrimaryKey(IncomeRecord record);

	/**
	 * 查询所有待返回本金和利息的记录
	 *
	 * @return
	 */
	List<IncomeRecord> selectAllRecordTobeReturn();

}