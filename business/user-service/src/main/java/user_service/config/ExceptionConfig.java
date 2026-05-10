package user_service.config;

import com.umar.exceptions.user.exceptionController.GlobalUserException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(GlobalUserException.class)
@Configuration
public class ExceptionConfig {

    @Bean
    public GlobalUserException userException(){
        return new GlobalUserException();
    }
}
