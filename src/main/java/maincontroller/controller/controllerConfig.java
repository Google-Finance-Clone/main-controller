package maincontroller.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import maincontroller.mqconfig.MQSender;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        System.out.println("test");
    }

    @PostMapping("/{microserviceName}")
    public void routeRequest(@PathVariable String microserviceName, HttpServletRequest request) {
        String routingKey = microserviceName.substring(0,microserviceName.length()-1) + "Sender";
        int instance= Integer.parseInt(microserviceName.substring(microserviceName.length()-1));
        String requestId = java.util.UUID.randomUUID().toString();
        String jsonRequest = convertRequestToJson(request,requestId);
        rabbitTemplate.convertSendAndReceiveAsType(routingKey, jsonRequest,new ParameterizedTypeReference<Gson>() {});
//        rabbitTemplate.convertAndSend(routingKey, jsonRequest);
    }

        private String convertRequestToJson(HttpServletRequest request, String requestId) {
        // Read the request body
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert the request body to a map
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> requestBodyMap = new HashMap<>();
        try {
            requestBodyMap = objectMapper.readValue(requestBody.toString(), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Extract the required attributes
        String correlationId = (String) requestBodyMap.get("correlationId");
        String method = (String) requestBodyMap.get("method");
        String route = (String) requestBodyMap.get("route");
        Object body = requestBodyMap.get("body");

        // Create a map to store request properties
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("correlationId", requestId);
        requestData.put("method", method);
        requestData.put("route", route);
        requestData.put("body", body);

        // Convert the map to JSON string
        try {
            return objectMapper.writeValueAsString(requestData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
