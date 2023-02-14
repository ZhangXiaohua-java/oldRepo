package cloud.huel.spike.mapper;

import cloud.huel.spike.domain.SpikeOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 张晓华
 * @since 2022-09-04
 */
@Mapper
public interface SpikeOrderMapper extends BaseMapper<SpikeOrder> {

	SpikeOrder selectOrderByUserAndGoodsId(@Param("gid") Integer goodsId, @Param("uid") Long userId);

	Integer selectStock(@Param("gid") Integer gid);

}
