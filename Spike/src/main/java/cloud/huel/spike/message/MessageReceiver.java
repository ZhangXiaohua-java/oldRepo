package cloud.huel.spike.message;

import cloud.huel.spike.constant.RedisConstants;
import cloud.huel.spike.domain.Order;
import cloud.huel.spike.domain.OrderMessage;
import cloud.huel.spike.service.IOrderService;
import cloud.huel.spike.service.ISpikeOrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author 张晓华
 * @date 2022-9-8
 */
@Service
@Slf4j
public class MessageReceiver {

	@Autowired
	private IOrderService orderService;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private ISpikeOrderService spikeOrderService;


	@RabbitListener(queues = {"spikeQueue"})
	public void receiveMessage(String msg) throws JsonProcessingException {
		OrderMessage message = new ObjectMapper().readValue(msg, OrderMessage.class);
		log.info("接收到消息{}", message);
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		Order order = (Order) ops.get(RedisConstants.SPIKE_ORDER_PREFIX + message.getUid() + ":" + message.getGid());
		if (!Objects.isNull(order)) {
			log.info("消息拦截 01 ");
			return;
		}
		if (spikeOrderService.getStock(message.getGid()) <=0 ) {
			log.info("消息拦截 02 ");
			return;
		}
		orderService.createNewOrder(message.getUid(), message.getGid());
	}


}
