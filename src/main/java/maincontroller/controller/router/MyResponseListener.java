import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class MyResponseListener implements ChannelAwareMessageListener {

    @Autowired
    private Jedis jedis;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String response = new String(message.getBody());

        // Store the response in Redis
        jedis.set(correlationId, response);

        // Acknowledge the message
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    public String getResponse(String correlationId) {
        return jedis.get(correlationId);
    }
}
