package com.myth.common.config.mq;

import com.myth.common.constant.RabbitConstants;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟队列配置
 *
 * @author zhangguixing Email:zhangguixing@qq.com
 */
@Configuration
public class TestDelayedQueueBuilder {

    public static final String QUEUE_NAME = "delay.order.queue";
    public static final String DELAYED_EXCHANGE_NAME = "delay.order.exchange";
    public static final String DELAYED_ROUTING_KEY = "delay.test.routing.key";

    // 死信
    public static final String DEAD_QUEUE_NAME = "dead.delay.order.queue";
    public static final String DEAD_EXCHANGE_NAME = "dead.delay.order.exchange";
    public static final String DEAD_ROUTING_KEY = "dead.delay.test.routing.key";

    @Bean(QUEUE_NAME)
    public Queue queue() {
        Map<String, Object> haArgs = new HashMap<>();
        haArgs.put(RabbitConstants.X_DEAD_LETTER_EXCHANGE, DEAD_EXCHANGE_NAME);
        haArgs.put(RabbitConstants.X_DEAD_LETTER_ROUTING_KEY, DEAD_ROUTING_KEY);
        return new Queue(QUEUE_NAME, true, false, false, haArgs);
    }

    @Bean(DELAYED_EXCHANGE_NAME)
    public CustomExchange exchange() {
        Map<String, Object> args = new HashMap<>();
        args.put(RabbitConstants.X_DELAYED_TYPE, RabbitConstants.DIRECT);
        return new CustomExchange(DELAYED_EXCHANGE_NAME, RabbitConstants.X_DELAYED_MESSAGE, true, false, args);
    }

    @Bean
    public Binding bindingOrderNotify(@Qualifier(QUEUE_NAME) Queue queue,
                                      @Qualifier(DELAYED_EXCHANGE_NAME) CustomExchange customExchange) {
        return BindingBuilder.bind(queue).to(customExchange).with(DELAYED_ROUTING_KEY).noargs();
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
    public Binding deadOrderQueueBinding(@Qualifier(DEAD_QUEUE_NAME) Queue queue,
                                         @Qualifier(DEAD_EXCHANGE_NAME) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}