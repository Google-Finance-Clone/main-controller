package maincontroller.controller;


import maincontroller.mqconfig.MQReciever;
import maincontroller.mqconfig.MQSender;
import maincontroller.services.ControllerService;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.naming.ldap.Control;
import java.util.UUID;

@RestController
@EnableRabbit
@RequestMapping
public class ControllerRouter {

    private final ControllerService controllerService;
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    MQSender mqSender;
    @Autowired
    MQReciever mqReciever;

    @Autowired
    public ControllerRouter(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    @PostMapping("/stocks/max-threads/{maxThreads}")
    public String setMaxThreads(@PathVariable int maxThreads) {
        String url = "http://localhost:8080/stocks/config/max-threads/" + maxThreads;
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }

    @PostMapping("/stocks/max-database-connections/{maxDatabaseConnections}")
    public String setMaxDatabaseConnections(@PathVariable int maxDatabaseConnections) {
        String url = "http://localhost:8080/stocks/config/max-database-connections/" + maxDatabaseConnections;
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }

    @PostMapping("/stocks/freeze")
    public String freezeStocks() {
        String url = "http://localhost:8080/stocks/config/freeze";
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }

    @GetMapping("/stocks/{restOfPath}")
    public String handleStocksRequest(@PathVariable String restOfPath) {
        UUID uuid = controllerService.generateUuid();
        String path = restOfPath;
        String Body = "";
        String message = "{uuid: " + uuid + ", path: " + path + ", body: " + Body + "}";
        mqSender.sendToStocks(message);
        return null;
    }

}
