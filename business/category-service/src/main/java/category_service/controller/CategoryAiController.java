package category_service.controller;

import category_service.serviceinterface.ICategoryAIService;
import com.umar.payload.request.category.ai.GenerateDescription;
import com.umar.payload.request.category.ai.SuggestCategory;
import com.umar.payload.response.category.ai.Suggestions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories/ai")
public class CategoryAiController {

    private final ICategoryAIService categoryAiService;

    @PostMapping("/suggest")
    public ResponseEntity<Suggestions> suggest(@RequestBody SuggestCategory request){
        Suggestions suggestions = this.categoryAiService.suggest(request);
        return ResponseEntity.ok(suggestions);
    }

    @PostMapping("/generate-description")
    public ResponseEntity<Suggestions> generateDescription(@RequestBody GenerateDescription request){
        Suggestions suggestions = this.categoryAiService.generateDescription(request);
        return null;
    }
}
