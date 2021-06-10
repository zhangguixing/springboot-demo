

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * rabbitmq整合
 *
 * @author zhangguixing Email:zhangguixing@co-mall.com
 * @since 2021-05-27 上午 10:18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class RabbitMQTest {
    @Resource(name = "ctsAmqpTemplate")
    private AmqpTemplate ctsAmqpTemplate;
    @Resource(name = "tcpAmqpTemplate")
    private AmqpTemplate tcpAmqpTemplate;


    @Test
    public void sendMessage(){
        ctsAmqpTemplate.convertAndSend("cts_test_queue","cts_test_queue Message");
        System.out.printf("a");
        tcpAmqpTemplate.convertAndSend("tcp_test_queue","tcp_test_queue Message");
    }
}