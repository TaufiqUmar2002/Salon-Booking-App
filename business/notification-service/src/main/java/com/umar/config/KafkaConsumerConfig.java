package com.umar.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

        @Bean
        public ConsumerFactory<String, Object> jsonConsumerFactory() {

            Map<String, Object> config = new HashMap<>();

            config.put(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                    "localhost:9092"
            );

            config.put(
                    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                    "earliest"
            );

            config.put(
                    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                    true
            );

            JacksonJsonDeserializer<Object> deserializer =
                    new JacksonJsonDeserializer<>(Object.class);

            deserializer.addTrustedPackages("com.umar.events.*");

            return new DefaultKafkaConsumerFactory<>(
                    config,
                    new StringDeserializer(),
                    deserializer
            );
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
            ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(jsonConsumerFactory());
            factory.getContainerProperties().setConsumerRebalanceListener(new ConsumerAwareRebalanceListener() {
                        @Override
                        public void onPartitionsAssigned(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
                            System.out.println("🔥 PARTITIONS ASSIGNED = " + partitions);
                        }
                        public void onPartitionsRevoked(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
                            System.out.println("❌ PARTITIONS REVOKED = " + partitions);
                        }
                    }
            );
            return factory;
        }

    @Bean
    public ConsumerFactory<String, String> stringConsumerFactory() {

        Map<String, Object> config = new HashMap<>();

        config.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9192,localhost:9193"
        );

        config.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "notification-group-test"
        );

        config.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest"
        );
        config.put(
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                true
        );

        JacksonJsonDeserializer<Object> deserializer =
                new JacksonJsonDeserializer<>(Object.class);

        deserializer.addTrustedPackages("com.umar.events.*");


        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                new StringDeserializer()
        );
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    stringKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(stringConsumerFactory());

        return factory;
    }

}
