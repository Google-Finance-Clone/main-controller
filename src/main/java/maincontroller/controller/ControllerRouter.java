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
@RequestMapping("ControllerRouter")
public class ControllerRouter {

//    private final ControllerService controllerService;
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    MQSender mqSender;
    @Autowired
    MQReciever mqReciever;

//    @Autowired
//    public ControllerRouter(ControllerService controllerService) {
//        this.controllerService = controllerService;
//    }

    @PostMapping("/{microservicename}/{instanceId}/max-threads/{maxThreads}")
    public String setMaxThreads(@PathVariable int maxThreads, @PathVariable int instanceId, @PathVariable String microservicename) {
        String url = "http://localhost:8888/"+microservicename+instanceId+"/config/max-threads/" + maxThreads;
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


    @PostMapping("/{microservicename}/{instanceId}/max-database-connections/{maxDatabaseConnections}")
    public String setMaxDatabaseConnections(@PathVariable int maxDatabaseConnections, @PathVariable int instanceId, @PathVariable String microservicename) {
        String url = "http://localhost:8080/"+microservicename+instanceId+"/config/max-database-connections/" + maxDatabaseConnections;
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }

    @PostMapping("/{microservicename}/{instanceId}/freeze/{maxDatabaseConnections}")
    public String freezeStocks(@PathVariable int maxDatabaseConnections, @PathVariable int instanceId, @PathVariable String microservicename) {
        String url = "http://localhost:8080/"+microservicename+instanceId+"/freeze";
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }

    @PostMapping("/{microservicename}/{instanceId}/unfreeze/{maxDatabaseConnections}")
    public String unfreezeStocks(@PathVariable int maxDatabaseConnections, @PathVariable int instanceId, @PathVariable String microservicename) {
        String url = "http://localhost:8080/"+microservicename+instanceId+"/unfreeze";
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }

    @PostMapping("/{microservicename}/{instanceId}/SetRabbitMQ/{ip}/{port}")
    public String setRabbitMQ(@PathVariable String ip, @PathVariable int port, @PathVariable String microservicename, @PathVariable int instanceId) {
        String url = "http://localhost:8080/"+microservicename+instanceId+"/config/MQAddress/" + ip + "/" + port;
        String response = restTemplate.getForObject(url, String.class);
        return response;
    }

    @PostMapping("/{microservicename}/{instanceId}/SetReportingLevel/{level}")
    public String setRabbitMQ(@PathVariable String level, @PathVariable String microservicename, @PathVariable int instanceId) {
        String url = "http://localhost:8080/"+microservicename+instanceId+"/set-error-reporting-level/"+level;
        String response = restTemplate.getForObject(url, String.class);
        return response;
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