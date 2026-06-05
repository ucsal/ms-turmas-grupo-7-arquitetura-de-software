package msturmas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"msturmas", "controller", "service", "repository", "entity", "dto", "exception", "client"})
@EnableFeignClients(basePackages = {"client"})
@EnableDiscoveryClient
public class MsTurmasApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsTurmasApplication.class, args);
    }
}
