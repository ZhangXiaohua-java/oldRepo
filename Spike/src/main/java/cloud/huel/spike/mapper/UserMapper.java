package cloud.huel.spike.mapper;

import cloud.huel.spike.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 张晓华
 * @since 2022-09-03
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
