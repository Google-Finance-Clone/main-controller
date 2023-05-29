package maincontroller.mqconfig;

import org.springframework.context.annotation.Bean;
//import queue from spring
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;

@Configuration
public class mqconfig {

    @Bean
    public Queue queue(){
        return new Queue("stocks");
    }
}
