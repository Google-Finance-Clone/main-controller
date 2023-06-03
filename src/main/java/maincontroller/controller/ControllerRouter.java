package maincontroller.controller;


import maincontroller.mqconfig.MQReciever;
import maincontroller.mqconfig.MQSender;
import maincontroller.services.ControllerService;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.naming.ldap.Control;
import java.util.UUID;

@RestController
@EnableRabbit
@RequestMapping("controller")
public class ControllerRouter {

//    private final ControllerService controllerService;
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    MQSender mqSender;
    @Autowired
    MQReciever mqReciever;
    //get value of stocks.port from application.properties
    @Value("${stocks.port}")
    private String stocksPort;
    //get value of portfolio.port from application.properties
    @Value("${portfolio.port}")
    private String portfolioPort;
    //get value of news.port from application.properties
    @Value("${news.port}")
    private String newsPort;

//    @Autowired
//    public ControllerRouter(ControllerService controllerService) {
//        this.controllerService = controllerService;
//    }

    @PostMapping("/{microservicename}/max-threads/{maxThreads}")
    public String setMaxThreads(@PathVariable int maxThreads, @PathVariable String microservicename) {
//        String url = "http://localhost:8888/"+microservicename+instanceId+"/config/max-threads/" + maxThreads;
        String url = "http://localhost:";
        //switch  over microservicename
        switch (microservicename) {
            case "stocks":
                url += stocksPort;
                break;
            case "portfolios":
                url += portfolioPort;
                break;
            case "news":
                url += newsPort;
                break;
            default:
                System.out.println("Invalid microservice name");
        }
        url += "/" +microservicename +"/config/max-threads/" + maxThreads;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(url,null, String.class);

        return response;
    }


//    @PostMapping("/{microservicename}/{instanceId}/max-threads/{maxThreads}")
//    public String setMaxThreads(@PathVariable int maxThreads, @PathVariable int instanceId, @PathVariable String microservicename) {
//
//        String url = "http://localhost:8888/"+microservicename+instanceId+"/config/max-threads/" + maxThreads;
//        String response = restTemplate.getForObject(url, String.class);
//        return response;
//    }


    @PostMapping("/{port}/{microservicename}/max-database-connections/{maxDatabaseConnections}")
    public String setMaxDatabaseConnections(@PathVariable int maxDatabaseConnections,  @PathVariable String microservicename, @PathVariable String port ) {
        String url = "http://localhost:" + port + "/"+microservicename+"/config/max-database-connections/" + maxDatabaseConnections;
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }

    @PostMapping("/{port}/{microservicename}/freeze/{maxDatabaseConnections}")
    public String freezeStocks(@PathVariable int maxDatabaseConnections, @PathVariable String microservicename, @PathVariable String port) {
        String url = "http://localhost:" + port + "/"+ microservicename +"/freeze";
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }

    @PostMapping("/{port}/{microservicename}/unfreeze/{maxDatabaseConnections}")
    public String unfreezeStocks(@PathVariable int maxDatabaseConnections, @PathVariable String microservicename,@PathVariable String port) {
        String url = "http://localhost:" + port + "/"+microservicename+"/unfreeze";
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }

    @PostMapping("/{serverPort}/{microservicename}/SetRabbitMQ/{ip}/{port}")
    public String setRabbitMQ(@PathVariable String ip, @PathVariable int port, @PathVariable String microservicename, @PathVariable int instanceId,@PathVariable int serverPort) {
        String url = "http://localhost:" + serverPort + "/"+ microservicename +"/config/MQAddress/" + ip + "/" + port;
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }

    @PostMapping("/{microservicename}/SetReportingLevel/{level}")
    public String setRabbitMQ(@PathVariable String level, @PathVariable String microservicename) {
        String url = "http://localhost:";
        //switch  over microservicename
        switch (microservicename) {
            case "stocks":
                url += stocksPort;
                break;
            case "portfolios":
                url += portfolioPort;
                break;
            case "news":
                url += newsPort;
                break;
            default:
                System.out.println("Invalid microservice name");
        }
        url += "/" +microservicename +"/config/set-error-reporting-level/" + level;

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(url,null, String.class);

        return response;
//        String url = "http://localhost:" + port + "/"+microservicename+instanceId+"/set-error-reporting-level/"+level;
//        String response = restTemplate.getForObject(url, String.class);
//        return response;
    }




//    @GetMapping("/stocks/{restOfPath}")
//    public String handleStocksRequest(@PathVariable String restOfPath) {
//        UUID uuid = controllerService.generateUuid();
//        String path = restOfPath;
//        String Body = "";
//        String message = "{uuid: " + uuid + ", path: " + path + ", body: " + Body + "}";
//        mqSender.sendToStocks(message);
//        return null;
//    }

}