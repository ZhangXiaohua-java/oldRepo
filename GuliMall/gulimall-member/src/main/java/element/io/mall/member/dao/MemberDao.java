package element.io.mall.member.dao;

import element.io.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author 张晓华
 * @email 3323393308@qq.com
 * @date 2022-10-27 20:45:39
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
