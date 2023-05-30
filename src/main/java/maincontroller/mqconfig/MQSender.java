package maincontroller.mqconfig;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQSender {

    @Autowired
    private Queue stocksSenderQueue;
    @Autowired
    private Queue portfoliosSenderQueue;
    @Autowired
    private Queue newsSenderQueue;
    @Autowired
    private Queue stocksRecieverQueue;
    @Autowired
    private Queue portfoliosRecieverQueue;
    @Autowired
    private Queue newsRecieverQueue;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String message, Queue queue){
        rabbitTemplate.convertAndSend(queue.getName(), message);
    }

    public void sendToStocks(String message) { rabbitTemplate.convertAndSend(stocksRecieverQueue.getName(), message);
    }
    public void sendToPortfolios(String message) { rabbitTemplate.convertAndSend(portfoliosRecieverQueue.getName(), message);
    }
    public void sendToNews(String message) { rabbitTemplate.convertAndSend(newsRecieverQueue.getName(), message);
    }
}
