package salon_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import salon_service.repository.SalonRepository;

import java.util.TimeZone;

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
public class SalonServiceApplication {

	public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(SalonServiceApplication.class, args);
	}

    @Bean
    CommandLineRunner test(
            SalonRepository repository) {

        return args -> {

            System.out.println(
                    repository.count()
            );
        };
    }

}
