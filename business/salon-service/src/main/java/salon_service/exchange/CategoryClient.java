package salon_service.exchange;

import com.umar.payload.response.category.CategoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CATEGORY-SERVICE",url = "http://localhost:5001",configuration = FeignJwtInterceptor.class)
public interface CategoryClient {

    @GetMapping("/api/categories/{id}")
    CategoryResponse getCategoryById(@PathVariable Long id);
}
