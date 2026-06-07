package com.umar.service_offering.exchange;

import com.umar.payload.response.category.LookupCategoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "CATEGORY-SERVICE",url = "http://localhost:5001")
public interface CategoryClient {

    @GetMapping("api/categories/internal/lookup")
    LookupCategoryResponse lookUpCategory(List<Long> idList);
}
