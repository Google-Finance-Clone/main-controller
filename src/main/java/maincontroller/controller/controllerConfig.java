package maincontroller.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import maincontroller.mqconfig.MQSender;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.*;


@EnableRabbit
@RestController
@RequestMapping("/api")
public class controllerConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    MQSender mqSender;

    @PostMapping
    public void test(){
        System.out.println();
    }

    @PostMapping("/{microserviceName}/**")
    public String routeRequest(@PathVariable String microserviceName, @RequestBody String requestBody, HttpServletRequest httpRequest) {

        Gson gson = new Gson();
        String URI = httpRequest.getRequestURI();
        String routingKey = microserviceName + "Sender1";
        String route = microserviceName+ URI.substring(5 + microserviceName.length());
        String method = "Post";
        JsonObject body =  gson.fromJson(requestBody, JsonObject.class);
        JsonObject jsonRequest = new JsonObject();

        System.out.println(body+"body");

        jsonRequest.addProperty("correlationId", "1234");
        jsonRequest.addProperty("method", method);
        jsonRequest.addProperty("route", route);
        jsonRequest.add("body", body);


        int instance= Integer.parseInt(microserviceName.substring(microserviceName.length()-1));
        String requestId = java.util.UUID.randomUUID().toString();
          System.out.println(requestBody+"request2");
        String response =rabbitTemplate.convertSendAndReceiveAsType(routingKey, jsonRequest.toString(),new ParameterizedTypeReference<String>() {});
        System.out.println(response+"response");
        return response;

    }

    @GetMapping("/{microserviceName}/**")
    public String routeGetRequest(@PathVariable String microserviceName, HttpServletRequest httpRequest){

        String URI = httpRequest.getRequestURI();
        String route = microserviceName+ URI.substring(5 + microserviceName.length());

        String method = "Get";
        String correlationId = "1234";
        JsonObject body = new JsonObject();
        JsonObject jsonRequest = new JsonObject();

        jsonRequest.addProperty("method", method);
        jsonRequest.addProperty("route", route);
        jsonRequest.addProperty("correlationId", correlationId);
        jsonRequest.add("body", body);

        String routingKey = microserviceName + "Sender1";
        String response =rabbitTemplate.convertSendAndReceiveAsType(routingKey, jsonRequest.toString(),new ParameterizedTypeReference<String>() {});
        System.out.println(response+"response");
        return response;

    }

    @DeleteMapping("/{microserviceName}/**")
    public String routeDeleteRequest(@PathVariable String microserviceName, HttpServletRequest httpRequest){
        String URI = httpRequest.getRequestURI();
        String route = microserviceName+ URI.substring(5 + microserviceName.length());

        String method = "Delete";
        String correlationId = "1234";
        JsonObject body = new JsonObject();
        JsonObject jsonRequest = new JsonObject();

        jsonRequest.addProperty("method", method);
        jsonRequest.addProperty("route", route);
        jsonRequest.addProperty("correlationId", correlationId);
        jsonRequest.add("body", body);

        String routingKey = microserviceName+ "Sender1";
        String response =rabbitTemplate.convertSendAndReceiveAsType(routingKey, jsonRequest.toString(),new ParameterizedTypeReference<String>() {});
        System.out.println(response+"response");
        return response;
    }

    @PutMapping("/{microserviceName}/**")
    public String routePutRequest(@PathVariable String microserviceName,@RequestBody String requestBody, HttpServletRequest httpRequest){
        Gson gson = new Gson();
        String URI = httpRequest.getRequestURI();
        String routingKey = microserviceName + "Sender1";
        String route = microserviceName + URI.substring(5 + microserviceName.length());
        String method = "Put";
        JsonObject body =  gson.fromJson(requestBody, JsonObject.class);
        JsonObject jsonRequest = new JsonObject();

        System.out.println(body+"body");

        jsonRequest.addProperty("correlationId", "1234");
        jsonRequest.addProperty("method", method);
        jsonRequest.addProperty("route", route);
        jsonRequest.add("body", body);


        String requestId = java.util.UUID.randomUUID().toString();
        System.out.println(requestBody+"request2");
        String response =rabbitTemplate.convertSendAndReceiveAsType(routingKey, jsonRequest.toString(),new ParameterizedTypeReference<String>() {});
        System.out.println(response+"response");
        return response;
    }
}