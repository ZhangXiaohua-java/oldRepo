package cloud.huel.spike.service.impl;

import cloud.huel.spike.constant.RedisConstants;
import cloud.huel.spike.domain.Order;
import cloud.huel.spike.domain.SpikeGoods;
import cloud.huel.spike.domain.SpikeOrder;
import cloud.huel.spike.ex.OrderException;
import cloud.huel.spike.mapper.GoodsMapper;
import cloud.huel.spike.mapper.OrderMapper;
import cloud.huel.spike.mapper.SpikeGoodsMapper;
import cloud.huel.spike.mapper.SpikeOrderMapper;
import cloud.huel.spike.service.IOrderService;
import cloud.huel.spike.service.ISpikeOrderService;
import cloud.huel.spike.vo.SpikeGoodsVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 张晓华
 * @since 2022-09-04
 */
@SuppressWarnings({"all"})
@Transactional(rollbackFor = {Throwable.class})
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {


	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private SpikeGoodsMapper spikeGoodsMapper;

	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private SpikeOrderMapper spikeOrderMapper;

	@Autowired
	private ISpikeOrderService spikeOrderService;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 创建请购订单
	 *
	 * @param id      用户id
	 * @param goodsId 商品id
	 * @return 新建订单详情
	 */
	@Override
	public Order createNewOrder(Long id, Integer goodsId) {
		// 查询秒杀商品信息
		SpikeGoods goods = spikeGoodsMapper.selectGoodsByGId(goodsId);
		// 查询该用户之前是否已经抢购过商品
		if (!Objects.isNull(redisTemplate.opsForValue().get(RedisConstants.SPIKE_ORDER_PREFIX + id +":" + goodsId))) {
			log.error("01 该用户已经参与过活动 {}, 商品ID{}", id, goodsId);
			//throw new OrderException("每个用户只允许参加一次活动");
			return null;
		}
		// 秒杀商品库存扣减 spike_goods表
		Integer rowCount = spikeGoodsMapper.subStock(goods.getId());
		if (rowCount == null || rowCount.equals(0)) {
			log.error("02 该用户已经参与过活动 {}, 商品ID{}", id, goodsId);
			//throw new OrderException("每个用户只允许参加一次活动");
			return null;
		}
		// 向t_order表中插入订单记录,生成订单记录
		Order order = new Order();
		order.setCreateTime(new Date());
		order.setAmount(1);
		order.setGoodsId(goodsId);
		order.setUserId(id);
		order.setStatus(0);
		// 商品数量为1,就不做计算了.
		order.setPayMoney(new BigDecimal(goods.getPrice()+""));
		int i = orderMapper.insertOrder(order);
		assert i == 1 : "生成订单失败";
		// 生成秒杀订单
		SpikeOrder sorder = new SpikeOrder();
		sorder.setOrderId(order.getId());
		sorder.setGoodsId(goodsId);
		sorder.setUserId(id);
		Integer count = spikeOrderMapper.insert(sorder);
		if (count == null || count.equals(0)) {
			log.error("03 该用户已经参与过活动 {}, 商品ID{}", id, goodsId);
			return null;
		}
		order = orderMapper.selectById(order.getId());
		// 将用户的订单记录存入到Redis中
		redisTemplate.opsForValue().set(RedisConstants.SPIKE_ORDER_PREFIX + id +":" + goodsId, order);
		return order;
	}


	@Override
	public Order queryOrderById(Integer id) {
		return orderMapper.selectById(id);
	}


}
