package pl.sda.carrental.configuration;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ConsoleUrlsAtStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String swaggerUrl = "http://localhost:8080/swagger-ui-car-rental.html";
        String H2ConsoleUrl = "http://localhost:8080/h2-console";
        System.out.println("Swagger UI is available at: " + swaggerUrl);
        System.out.println("(Test profile) H2 Console UI is available at: " + H2ConsoleUrl);
    }
}
