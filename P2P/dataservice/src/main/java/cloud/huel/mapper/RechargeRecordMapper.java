package cloud.huel.mapper;

import cloud.huel.domain.load.RechargeRecord;

public interface RechargeRecordMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(RechargeRecord record);

	int insertSelective(RechargeRecord record);

	RechargeRecord selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(RechargeRecord record);

	int updateByPrimaryKey(RechargeRecord record);
}