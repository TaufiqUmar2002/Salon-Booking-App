package category_service.mapper;

import category_service.model.Category;
import com.umar.payload.request.category.CreateCategoryRequest;
import com.umar.payload.response.category.CategoryResponse;
import com.umar.payload.response.category.LookupCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CreateCategoryRequest request);

    @Mapping(target = "categoryId",source = "id")
    CategoryResponse toResponse(Category category);

    List<CategoryResponse> toDtoList(List<Category> categories);

    @Mapping(target = "categoryId",source = "id")
    LookupCategory toLookUpCategory(Category category);

}
