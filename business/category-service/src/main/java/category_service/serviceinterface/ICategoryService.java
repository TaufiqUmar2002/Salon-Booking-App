package category_service.serviceinterface;


import com.umar.payload.request.category.CreateCategoryRequest;
import com.umar.payload.request.category.DeleteCategoryRequest;
import com.umar.payload.request.category.MergeCategoryRequest;
import com.umar.payload.request.category.UpdateCategoryRequest;
import com.umar.payload.response.category.CategoryResponse;
import com.umar.payload.response.category.CategoryResponseList;
import com.umar.payload.response.category.LookupCategoryResponse;

import java.util.List;

public interface ICategoryService {

    CategoryResponse createCategory(CreateCategoryRequest request);
    CategoryResponseList getAllCategory(String format,Boolean topLevelOnly,Boolean featuredOnly,Boolean includeCount);
    CategoryResponse  getCategoryById(Long id);
    CategoryResponse updateCategory(Long id, UpdateCategoryRequest request);
    void deleteCategory(Long id, DeleteCategoryRequest request);
    CategoryResponse restoreCategory(Long id);
    CategoryResponse mergeCategory(Long id, MergeCategoryRequest request);
    LookupCategoryResponse lookupCategory(List<String> idList);

}
