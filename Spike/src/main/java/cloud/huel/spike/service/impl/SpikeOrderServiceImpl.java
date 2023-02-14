package cloud.huel.spike.service.impl;

import cloud.huel.spike.domain.SpikeOrder;
import cloud.huel.spike.mapper.SpikeOrderMapper;
import cloud.huel.spike.service.ISpikeOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 张晓华
 * @since 2022-09-04
 */
@Service
public class SpikeOrderServiceImpl extends ServiceImpl<SpikeOrderMapper, SpikeOrder> implements ISpikeOrderService {

	@Autowired
	private SpikeOrderMapper spikeOrderMapper;


	/**
	 * 查看指定的用户是否已经请购过指定的商品.
	 *
	 * @param goodsId     商品ID
	 * @param userId 用户ID
	 * @return
	 */
	@Override
	public SpikeOrder queryExistingOrderOfUser(Integer goodsId, Long userId) {
		SpikeOrder order = spikeOrderMapper.selectOrderByUserAndGoodsId(goodsId, userId);
		return order;
	}


	@Override
	public boolean addNewSpikeOrder(SpikeOrder order) {
		int i = spikeOrderMapper.insert(order);
		return i == 1;
	}

	public Integer getStock(Integer gid) {
		return spikeOrderMapper.selectStock(gid);
	}

}
