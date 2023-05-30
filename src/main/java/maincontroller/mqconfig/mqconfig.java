package maincontroller.mqconfig;

import org.springframework.context.annotation.Bean;
//import queue from spring
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;

@Configuration
public class mqconfig {

    @Bean
    public Queue stocksRecieverQueue(){
        return new Queue("stocks-reciever");
    }
    @Bean
    public Queue portfoliosRecieverQueue(){
        return new Queue("portfolios-reciever");
    }
    @Bean
    public Queue stocksSenderQueue(){
        return new Queue("stocks-sender");
    }
    @Bean
    public Queue portfoliosSenderQueue(){
        return new Queue("portfolios-sender");
    }
    @Bean
    public Queue newsSenderQueue(){
        return new Queue("news-sender");
    }
    @Bean
    public Queue newsRecieverQueue(){
        return new Queue("news-reciever");
    }

}
