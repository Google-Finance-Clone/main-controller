package maincontroller.controller.router;

import org.springframework.context.annotation.Bean;
//import queue from spring
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;

@Configuration
public class mqconfig {

    @Bean
    public Queue stocksQueue(){
        return new Queue("stocksReciever");
    }

    @Bean
    public Queue portfoliosQueue(){
        return new Queue("portfoliosReciever");
    }

    @Bean
    public Queue searchQueue(){
        return new Queue("searchReciever");
    }

    @Bean
    public Queue newsQueue(){
        return new Queue("newsReciever");
    }

}
