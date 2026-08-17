package category_service;

import category_service.serviceinterface.ICategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
public class CategoryServiceApplication {


	public static void main(String[] args) {
		SpringApplication.run(CategoryServiceApplication.class, args);

    }


    @Bean
    CommandLineRunner test(ICategoryService categoryService) {
        return args -> {

            System.out.println(categoryService+".........................");

        };
    }

}
