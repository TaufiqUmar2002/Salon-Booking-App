package user_service.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(OllamaChatModel ollamaChatModel){

        return ChatClient.builder(ollamaChatModel).
                defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultOptions(OllamaChatOptions.builder()
                        .model("deepseek-r1:1.5b")
                        .temperature(0.7)
                        .maxTokens(1000)
                        .build()
                ).defaultSystem("You are a good ").
                build();
    }
}
