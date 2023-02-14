package cloud.huel.spike.service;

import cloud.huel.spike.domain.SpikeGoods;
import cloud.huel.spike.vo.SpikeGoodsVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 张晓华
 * @since 2022-09-04
 */
public interface ISpikeGoodsService extends IService<SpikeGoods> {

	/**
	 * 查询所有的秒杀商品
	 * @return
	 */
	List<SpikeGoodsVO> querySpikeGoodsForPage();

	/**
	 * 查询秒杀商品详情
	 * @param id 商品ID
	 * @return 商品详情
	 */
	SpikeGoodsVO querySpikeGoodsByGooodsId(Integer id);



}
