package category_service.service;


import category_service.model.Category;
import category_service.repository.CategoryRepository;
import category_service.serviceinterface.ICategoryAIService;
import com.umar.payload.request.category.ai.GenerateDescription;
import com.umar.payload.request.category.ai.SuggestCategory;
import com.umar.payload.response.category.ai.Suggestions;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryAiService implements ICategoryAIService {


    private final ChatClient chatClient;
    @Value("classpath:prompts/category-prompt.st")
    private  Resource promptResource;
    private final CategoryRepository categoryRepository;;


    @Override
    public Suggestions suggest(SuggestCategory category) {
        List<Category> categories = categoryRepository.findAll();
        String categoryList = categories.stream()
                .map(c -> c.getId() + " - " + c.getName())
                .collect(Collectors.joining("\n"));

         return  chatClient.prompt()
                .user(prompt->
                        prompt.text(promptResource)
                                .param("name", category.getServiceName())
                                .param("description", category.getServiceDescription())
                                .param("categories", categoryList)
                        )
                .call().entity(Suggestions.class);
    }

    @Override
    public Suggestions generateDescription(GenerateDescription description) {
        return null;
    }
}
