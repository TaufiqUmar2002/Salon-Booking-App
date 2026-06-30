package salon_service.config;

import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class SalonExecutorConfig {

    public Executor executor() {
        return  Executors.newFixedThreadPool(10);
    }
}
