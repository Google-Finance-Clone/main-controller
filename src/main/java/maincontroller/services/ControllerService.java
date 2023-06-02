package maincontroller.services;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Service
public class ControllerService {


    public UUID generateUuid() {
        UUID uuid = UUID.randomUUID();
       return uuid;
    }

}
