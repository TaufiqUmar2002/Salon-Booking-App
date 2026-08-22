package category_service.mapper;

import category_service.model.Category;
import com.umar.payload.request.category.CreateCategoryRequest;
import com.umar.payload.response.category.CategoryResponse;
import com.umar.payload.response.category.LookupCategory;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-22T22:00:58+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category toEntity(CreateCategoryRequest request) {
        if ( request == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        category.name( request.getName() );
        category.description( request.getDescription() );
        category.iconUrl( request.getIconUrl() );
        category.displayOrder( request.getDisplayOrder() );
        category.isFeatured( request.getIsFeatured() );
        category.metaTitle( request.getMetaTitle() );
        category.metaDescription( request.getMetaDescription() );

        return category.build();
    }

    @Override
    public CategoryResponse toResponse(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryResponse.CategoryResponseBuilder categoryResponse = CategoryResponse.builder();

        categoryResponse.categoryId( category.getId() );
        categoryResponse.name( category.getName() );
        categoryResponse.slug( category.getSlug() );
        categoryResponse.level( category.getLevel() );
        categoryResponse.iconUrl( category.getIconUrl() );
        categoryResponse.displayOrder( category.getDisplayOrder() );
        categoryResponse.isFeatured( category.getIsFeatured() );
        categoryResponse.isActive( category.getIsActive() );
        categoryResponse.salonCount( category.getSalonCount() );
        categoryResponse.bookingCount( category.getBookingCount() );

        return categoryResponse.build();
    }

    @Override
    public List<CategoryResponse> toDtoList(List<Category> categories) {
        if ( categories == null ) {
            return null;
        }

        List<CategoryResponse> list = new ArrayList<CategoryResponse>( categories.size() );
        for ( Category category : categories ) {
            list.add( toResponse( category ) );
        }

        return list;
    }

    @Override
    public LookupCategory toLookUpCategory(Category category) {
        if ( category == null ) {
            return null;
        }

        LookupCategory.LookupCategoryBuilder lookupCategory = LookupCategory.builder();

        lookupCategory.categoryId( category.getId() );
        lookupCategory.name( category.getName() );
        lookupCategory.slug( category.getSlug() );
        lookupCategory.isActive( category.getIsActive() );

        return lookupCategory.build();
    }
}
