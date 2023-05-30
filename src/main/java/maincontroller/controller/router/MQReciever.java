package maincontroller.controller.router;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MQReciever {

    @RabbitListener(queues = {"stocks"})
    public void receive(@Payload String message){
        System.out.println("Received message: "+message);
    }

}
