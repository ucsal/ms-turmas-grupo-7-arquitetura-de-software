package msturmas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsTurmasApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsTurmasApplication.class, args);
    }
}