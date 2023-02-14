package cloud.huel.spike.message;

import cloud.huel.spike.domain.OrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 张晓华
 * @date 2022-9-8
 */
@Service
@Slf4j
public class MessageSender {


	@Autowired
	private AmqpTemplate amqpTemplate;


	public void sendMessage(String msg) {
		log.info("发送消息{}", msg);
		amqpTemplate.convertAndSend("spikeExchange", "spike.order", msg);
	}




}
