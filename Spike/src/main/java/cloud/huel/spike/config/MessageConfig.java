package cloud.huel.spike.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 张晓华
 * @date 2022-9-8
 */
@Configuration
public class MessageConfig {

	@Bean
	public Queue spikeQueue() {
		return new Queue("spikeQueue", true, false, false);
	}

	@Bean
	public Exchange spikeExchange() {
		return new TopicExchange("spikeExchange", true, false);
	}



	@Bean
	public Binding binding(Queue spikeQueue, Exchange spikeExchange) {
		return BindingBuilder.bind(spikeQueue)
				.to(spikeExchange)
				.with("spike.#")
				.noargs();
	}




}
