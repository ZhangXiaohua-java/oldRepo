package cloud.huel.spike.mapper;

import cloud.huel.spike.domain.SpikeGoods;
import cloud.huel.spike.vo.SpikeGoodsVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 张晓华
 * @since 2022-09-04
 */
@Mapper
public interface SpikeGoodsMapper extends BaseMapper<SpikeGoods> {

	List<SpikeGoodsVO> selectAllSpikeGoodsForPage();


	SpikeGoodsVO selectGoodsByGoodsId(@Param("id") Integer gid);

	Integer subStock(Integer goodsId);

	SpikeGoods selectGoodsByGId(@Param("gid") Integer id);



}
