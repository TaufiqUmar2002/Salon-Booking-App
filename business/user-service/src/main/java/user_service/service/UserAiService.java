package user_service.service;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import user_service.serviceInterface.IUserAiService;

@Service
@RequiredArgsConstructor
public class UserAiService implements IUserAiService {

    @Value("classpath:prompts/system/salon-system.st")
    private Resource systemPromptResource;
    @Value("classpath:prompts/user/salon-recommendation.st")
    private Resource userPromptResource;




}
