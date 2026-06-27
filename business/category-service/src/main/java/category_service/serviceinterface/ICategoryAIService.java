package category_service.serviceinterface;

import com.umar.payload.request.category.ai.GenerateDescription;
import com.umar.payload.request.category.ai.SuggestCategory;
import com.umar.payload.response.category.ai.Suggestions;

public interface ICategoryAIService {

    Suggestions suggest(SuggestCategory category);
    Suggestions generateDescription(GenerateDescription description);
}
