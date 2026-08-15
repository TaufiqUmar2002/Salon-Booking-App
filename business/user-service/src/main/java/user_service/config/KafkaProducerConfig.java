package user_service.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, Object> producerFactory(){
        Map<String,Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9192");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class );
        config.put(ProducerConfig.LINGER_MS_CONFIG, 1000);
        config.put(ProducerConfig.BATCH_SIZE_CONFIG,32768);
        config.put(ProducerConfig.BUFFER_MEMORY_CONFIG,33554432);
        config.put(ProducerConfig.MAX_BLOCK_MS_CONFIG,60000);
        config.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,"snappy");
        config.put(ProducerConfig.ACKS_CONFIG,"all");
        config.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,2000);
        config.put(ProducerConfig.RETRIES_CONFIG,1);
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,3);
        return new DefaultKafkaProducerFactory<>(config);
    }
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
