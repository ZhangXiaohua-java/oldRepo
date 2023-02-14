package cloud.huel.spike.controller;

import cloud.huel.spike.annotation.AccessLimit;
import cloud.huel.spike.constant.RedisConstants;
import cloud.huel.spike.constant.UserConstants;
import cloud.huel.spike.domain.*;
import cloud.huel.spike.ex.SpikeException;
import cloud.huel.spike.domain.OrderMessage;
import cloud.huel.spike.message.MessageSender;
import cloud.huel.spike.pub.ResponseStatus;
import cloud.huel.spike.service.IGoodsService;
import cloud.huel.spike.service.IOrderService;
import cloud.huel.spike.service.ISpikeGoodsService;
import cloud.huel.spike.service.ISpikeOrderService;
import cloud.huel.spike.vo.ResultVO;
import cloud.huel.spike.vo.SpikeGoodsVO;
import cloud.huel.spike.vo.SpikeOrderVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author 张晓华
 * @date 2022-9-3
 */
@SuppressWarnings({"all"})
@Slf4j
@Controller
@RequestMapping("/goods")
public class GoodsController implements InitializingBean {

	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private ISpikeGoodsService spikeGoodsService;

	@Autowired
	private ISpikeOrderService spikeOrderService;

	@Autowired
	private IOrderService orderService;


	@Autowired
	private ThymeleafViewResolver viewResolver;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private volatile HashMap<Integer, Boolean> map = new HashMap<>();

	@Autowired
	private MessageSender sender;

	/**
	 * 静态化之前TPS: 232
	 * 静态化之后:    314
	 * @param formatter
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/list")
	@ResponseBody
	public ResultVO goods(DateTimeFormatter formatter) {
		String loginTime = formatter.format(LocalDateTime.now());
		List<SpikeGoodsVO> goodsVOS = spikeGoodsService.querySpikeGoodsForPage();
		return ResultVO.success(ResponseStatus.SUCCESS)
				.addData("loginTime", loginTime)
				.addData("goods", goodsVOS);
	}


	@GetMapping(value = "/detail/{goodsId}")
	@ResponseBody
	public ResultVO goodsDetail(@PathVariable Integer goodsId) {
		SpikeGoodsVO goods = spikeGoodsService.querySpikeGoodsByGooodsId(goodsId);
		// 定义一个秒杀活动开始的标志量, -1 未开始,0正在进行中,1已结束
		long timeDiff = 0L;
		int flag = -1;
		if (new Date().before(goods.getStartTime())) {
			timeDiff = (goods.getStartTime().getTime() - new Date().getTime()) / 1000;
		} else if (new Date().after(goods.getStartTime()) && new Date().before(goods.getEndTime())) {
			flag = 0;
 		} else {
			flag = 1;
		}
		return ResultVO.success(ResponseStatus.SUCCESS).addData("goods", goods)
				.addData("flag", flag)
				.addData("time", timeDiff);
	}


	/**
	 *  Redis + RabbitMQ 异步处理 + 内存标记减少对Redis的访问.
	 * @param session
	 * @param goodsId
	 * @param model
	 * @return
	 */
	@PostMapping("/spike/{path}/{goodsId}")
	@ResponseBody
	public ResultVO spike(HttpSession session,
						  @PathVariable String path,
						  @PathVariable Integer goodsId,
						  Model model) throws JsonProcessingException {
		//// 库存判断
		//SpikeGoodsVO goodsVO = spikeGoodsService.querySpikeGoodsByGooodsId(goodsId);
		//Integer stock = goodsVO.getStock();
		//if (Objects.isNull(stock) || stock <= 0) {
		//	throw new SpikeException(ResponseStatus.SPIKE_EXCEPTION.getMessage());
		//}
		// 内存标记,减少对Redis的访问量
		// 用布尔值来表示商品是否还有剩余库存,false代表没有库存,true则代表仍有库存,如果没有库存则直接返回结果提示库存售罄
		User user = (User) session.getAttribute(UserConstants.USER_KEY);
		if (!goodsService.checkPath(user.getId().intValue(), goodsId, path)) {
			return ResultVO.error(ResponseStatus.ILLEGAL);
		}
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		if (!map.get(goodsId)) {
			log.info("Map拦截掉用户的秒杀请求");
			return ResultVO.error(ResponseStatus.SOLD_OUT).addData("status", -1);
		}
		Long stock = ops.decrement(RedisConstants.SPIKE_GOODS_PREFIX + goodsId + ":stock");
		if (stock < 0 ) {
			ops.increment(RedisConstants.SPIKE_GOODS_PREFIX + goodsId + ":stock");
			log.info("商品库存为0之后进来秒杀的商品ID {}", goodsId);
			// 当前秒杀的商品库存为0,则直接将这个商品标记为false
			map.put(goodsId, false);
			return ResultVO.error(ResponseStatus.SOLD_OUT).addData("status", -1);
		}
		log.info("参与秒杀的用户ID {},秒杀的商品ID {}", user.getId(), goodsId);
		Order order = (Order) ops.get(RedisConstants.SPIKE_ORDER_PREFIX + user.getId() + ":" + goodsId);
		if (!Objects.isNull(order)) {
			return ResultVO.error(ResponseStatus.REPEATED_SPIKE).addData("status", 2);
		}
		//// 生成用户订单
		//Order newOrder = orderService.createNewOrder(user.getId(),goodsId);
		// 通过RabbitMQ异步处理订单信息
		OrderMessage message = new OrderMessage(user.getId(), goodsId);
		ObjectMapper mapper = new ObjectMapper();
		String msg = mapper.writeValueAsString(message);
		sender.sendMessage(msg);
		// 0 代表排队中, -1 代表库存为0,秒杀失败, 1 代表秒杀成功, 2代表重复秒杀
		return ResultVO.success(ResponseStatus.SUCCESS).addData("status", 0);
	}


	@GetMapping("/order/{id}")
	@ResponseBody
	public ResultVO queryOrderDetail(HttpSession session , @PathVariable Integer id) {
		User user = (User) session.getAttribute(UserConstants.USER_KEY);
		SpikeOrder o = spikeOrderService.queryExistingOrderOfUser(id, user.getId());
		Order order = orderService.queryOrderById(o.getOrderId());
		if (order == null) {
			throw new SpikeException(ResponseStatus.NOT_EXIST_ORDER.getMessage());
		}
		Goods goods = goodsService.queryGoodsById(order.getGoodsId());
		SpikeOrderVO vo = new SpikeOrderVO();
		vo.setGoodsName(goods.getGoodsName());
		vo.setImgAddress(goods.getImgAddress());
		vo.setPayMoney(order.getPayMoney());
		vo.setCreateTime(order.getCreateTime());
		vo.setStatus(order.getStatus());
		return ResultVO.success(ResponseStatus.SUCCESS).addData("order", vo);
	}

	@AccessLimit(duration = 10, count = 5)
	@GetMapping("/path")
	@ResponseBody
	public ResultVO getPath(HttpSession session, Integer gid, String code) {
		log.info("获取到的code " + code);
		if (Objects.isNull(gid) || Objects.isNull(code)) {
			return ResultVO.error(ResponseStatus.ERROR);
		}
		User user = (User) session.getAttribute(UserConstants.USER_KEY);
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		String storageCode = (String) ops.get(UserConstants.CODE_PREFIX + user.getId().intValue() + ":" + gid);
		if (!code.equals(storageCode)) {
			return ResultVO.error(ResponseStatus.CODE_NOT_MATCH);
		}
		String path = goodsService.generatePath(user.getId().intValue(), gid);
		return ResultVO.success(ResponseStatus.SUCCESS).addData("path", path);
	}

	@GetMapping("/order/result")
	@ResponseBody
	public ResultVO result(HttpSession session, Integer id) {
		User user = (User) session.getAttribute(UserConstants.USER_KEY);
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		if (!Objects.isNull(ops.get(RedisConstants.SPIKE_ORDER_PREFIX + user.getId() + ":" + id))) {
			return ResultVO.success(ResponseStatus.SUCCESS).addData("status", 1);
		} else if (map.get(id)){
			return ResultVO.success(ResponseStatus.WAITING).addData("status", -1);
		} else {
			return ResultVO.success(ResponseStatus.WAITING).addData("status", 0);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		List<SpikeGoodsVO> list = spikeGoodsService.querySpikeGoodsForPage();
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		list.forEach(e->{
			if (e.getStock() > 0) {
				map.put(e.getId(),true);
			} else {
				map.put(e.getId(),false);
			}
			ops.set(RedisConstants.SPIKE_GOODS_PREFIX + e.getId() + ":stock", e.getStock());
		});
	}


}
