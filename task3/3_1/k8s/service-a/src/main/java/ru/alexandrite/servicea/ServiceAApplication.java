package ru.alexandrite.servicea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class ServiceAApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAApplication.class, args);
    }
}

@RestController
class ServiceAController {

    private final RestClient restClient = RestClient.create();

    @GetMapping("/")
    public String handle() {
        String response = restClient
                .get()
                .uri("http://service-b:8080/")
                .retrieve()
                .body(String.class);

        return "service-a -> " + response;
    }
}