package cloud.huel.spike.service;

import cloud.huel.spike.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 张晓华
 * @since 2022-09-04
 */
public interface IOrderService extends IService<Order> {

	/**
	 * 创建请购订单
	 * @param id 用户id
	 * @param goodsId 商品id
	 * @return 新建订单详情
	 */
	Order createNewOrder(Long id, Integer goodsId);

	Order queryOrderById(Integer id);


}
