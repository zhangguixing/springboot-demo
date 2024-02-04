package com.myth.common.config.mq;

import com.myth.common.constant.RabbitConstants;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangguixing Email:guixingzhang@qq.com
 */
@Configuration
public class TestQueueBuilder {

    public static final String QUEUE_NAME = "order.queue";
    public static final String EXCHANGE_NAME = "order.exchange";
    public static final String ROUTING_KEY_NAME = "order.test.routing.key";

    public static final String DEAD_QUEUE_NAME = "dead.order.queue";
    public static final String DEAD_EXCHANGE_NAME = "dead.order.exchange";
    public static final String DEAD_ROUTING_KEY_NAME = "dead.test.routing.key";


    @Bean(QUEUE_NAME)
    public Queue queue() {
        Map<String, Object> haArgs = new HashMap<>();
        haArgs.put(RabbitConstants.X_DEAD_LETTER_EXCHANGE, DEAD_EXCHANGE_NAME);
        haArgs.put(RabbitConstants.X_DEAD_LETTER_ROUTING_KEY, DEAD_ROUTING_KEY_NAME);
        return new Queue(QUEUE_NAME, true, false, false, haArgs);
    }

    @Bean(EXCHANGE_NAME)
    public Exchange exchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).build();
    }

    @Bean
    public Binding orderQueueBinding(@Qualifier(QUEUE_NAME) Queue queue,
                                     @Qualifier(EXCHANGE_NAME) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_NAME).noargs();
    }


    // 死信配置
    @Bean(DEAD_QUEUE_NAME)
    public Queue deadQueue() {
        return new Queue(DEAD_QUEUE_NAME);
    }


    @Bean(DEAD_EXCHANGE_NAME)
    public Exchange deadExchange() {
        return new DirectExchange(DEAD_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Binding orderDeadBinding(@Qualifier(DEAD_QUEUE_NAME) Queue queue,
                                    @Qualifier(DEAD_EXCHANGE_NAME) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DEAD_ROUTING_KEY_NAME).noargs();
    }

}
