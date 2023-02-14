package cloud.huel.spike.service;

import cloud.huel.spike.domain.SpikeOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 张晓华
 * @since 2022-09-04
 */
public interface ISpikeOrderService extends IService<SpikeOrder> {

	/**
	 *  查看指定的用户是否已经请购过指定的商品.
	 * @param goodsId  商品ID
	 * @param userId  用户ID
	 * @return
	 */
	SpikeOrder queryExistingOrderOfUser(Integer goodsId, Long userId);

	boolean addNewSpikeOrder(SpikeOrder order);


	Integer getStock(Integer gid);

}
