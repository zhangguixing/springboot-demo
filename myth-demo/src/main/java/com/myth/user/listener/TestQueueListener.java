package com.myth.user.listener;

import com.myth.common.config.mq.TestDelayedQueueBuilder;
import com.myth.common.config.mq.TestQueueBuilder;
import com.myth.common.util.JSONUtils;
import com.myth.user.application.event.TestEvent;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class TestQueueListener {

    @RabbitListener(queues = TestQueueBuilder.QUEUE_NAME)
    public void receiveMessage(TestEvent event, Message message, Channel channel) throws IOException, InterruptedException {
        System.out.println("=====:" + JSONUtils.to(event));
    }

    @RabbitListener(queues = TestDelayedQueueBuilder.QUEUE_NAME)
    public void receiveDelayMessage(TestEvent event, Message message, Channel channel) throws IOException, InterruptedException {
        System.out.println("delay=====:" + JSONUtils.to(event));
    }
}
