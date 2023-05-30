package maincontroller.controller.router;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Controller
public class Router {

    @Autowired
    private Queue queue;

    @Autowired
    private ReactiveRedisOperations<String, String> reactiveRedisTemplate;

    // Define the missing variables
    private int maxAttempts = 3;
    private Duration firstBackoff = Duration.ofMillis(100);
    private Duration maxBackoff = Duration.ofMillis(1000);
    private double backoffMultiplier = 2.0;

    @PostMapping("/my-endpoint")
    @ResponseBody
    public Mono<String> processRequest(@RequestBody RequestData requestData) {
        // Generate a unique correlation ID
        String correlationId = UUID.randomUUID().toString();



        // Store the correlation ID in Redis with an initial value
        ReactiveValueOperations<String, String> valueOperations = reactiveRedisTemplate.opsForValue();
        Mono<Boolean> setOperation = valueOperations.set(correlationId, "PENDING");

        // Wait for the response asynchronously
        return setOperation
                .then(valueOperations.get(correlationId))
                .repeatWhenEmpty(Retry.backoff(maxAttempts, firstBackoff, maxBackoff, backoffMultiplier))
                .filter(response -> !response.equals("PENDING"))
                .doOnNext(response -> valueOperations.delete(correlationId))
                .timeout(Duration.ofMillis(5000))
                .switchIfEmpty(Mono.error(new TimeoutException("Response not received within timeout")))
                .onErrorReturn(error -> "Error: " + error.toString());

    }
}
