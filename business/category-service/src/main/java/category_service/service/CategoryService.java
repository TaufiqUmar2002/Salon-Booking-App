package category_service.service;


import category_service.event.CategoryEventProducer;
import category_service.exchange.UserClient;
import category_service.mapper.CategoryMapper;
import category_service.model.Category;
import category_service.model.CategoryAuditLog;
import category_service.repository.CategoryAuditLogRepository;
import category_service.repository.CategoryRepository;
import category_service.serviceinterface.ICategoryService;
import com.umar.events.category.CreateCategoryEventRequest;
import com.umar.events.category.UpdateCategoryEventRequest;
import com.umar.exceptions.common.exception.ApiException;
import com.umar.payload.request.category.CreateCategoryRequest;
import com.umar.payload.request.category.DeleteCategoryRequest;
import com.umar.payload.request.category.MergeCategoryRequest;
import com.umar.payload.request.category.UpdateCategoryRequest;
import com.umar.payload.request.user.UserValidateResponse;
import com.umar.payload.response.category.CategoryResponse;
import com.umar.payload.response.category.CategoryResponseList;
import com.umar.payload.response.category.LookupCategory;
import com.umar.payload.response.category.LookupCategoryResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final UserClient userClient;
    private final CategoryMapper categoryMapper;
    private final CategoryAuditLogRepository categoryAuditLogRepository;
    private final CategoryEventProducer eventProducer;

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        UserValidateResponse response = userClient.getUserValidation();
        Optional<Category> category= categoryRepository.findCategoryByName(request.getName());
        if(category.isPresent()){
            throw new ApiException(HttpStatus.BAD_REQUEST,"DUPLICATE_NAME","A category with this name already exists at this level");
        }
        Category persistCategory = categoryMapper.toEntity(request);
        if(request.getParentId()!=null){
            Optional<Category> parentCategory = categoryRepository.findById(request.getParentId());
            if(parentCategory.isEmpty()){
                throw new ApiException(HttpStatus.BAD_REQUEST,"PARENT_NOT_FOUND","Parent category not found");
            }
            parentCategory.ifPresent(persistCategory::setParent);
        }
        persistCategory.setCreatedBy(response.getUserId());
        persistCategory.setIsActive(true);
        persistCategory.setCreatedAt(LocalDateTime.now());
        Category savedCategory  =categoryRepository.save(persistCategory);
        CategoryAuditLog auditLog = new CategoryAuditLog();
        auditLog.setAction("CREATED");
        auditLog.setPerformedAt(LocalDateTime.now());
        auditLog.setCategoryId(savedCategory.getId());
        auditLog.setReason("new created");
        auditLog.setAdminId(response.getUserId());
        categoryAuditLogRepository.save(auditLog);
        CreateCategoryEventRequest eventRequest =CreateCategoryEventRequest.builder()
                .categoryId(savedCategory.getId())
                .description(savedCategory.getDescription())
                .createdAt(LocalDateTime.now())
                .build();
        eventProducer.publishCategoryCreationEvent(eventRequest);
        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public CategoryResponseList getAllCategory(String format,  Boolean topLevelOnly, Boolean featuredOnly, Boolean includeCount) {
        if (!List.of("flat", "tree").contains(format)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "INVALID_FORMAT",
                    "format must be either flat or tree"
            );
        }
        List<Category> categoryEntities = categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
        Stream<Category> stream =categoryEntities.stream();
        if(Boolean.TRUE.equals(featuredOnly)){
            stream = stream.filter(Category::getIsFeatured);
        }
        if(Boolean.TRUE.equals(topLevelOnly)){
            stream = stream.filter(c -> c.getLevel() == 0);
        }
        List<CategoryResponse> categoryDtos = stream
                .map(category -> mapToDto(category, includeCount))
                .sorted(Comparator.comparing(CategoryResponse::getDisplayOrder))
                .toList();
        if ("flat".equalsIgnoreCase(format)) {
            return CategoryResponseList.builder()
                    .categories(categoryDtos)
                    .totalCategories(categoryDtos.size())
                    .build();
        }
        List<CategoryResponse> tree = buildTree(categoryDtos);
        return CategoryResponseList.builder()
                .categories(tree)
                .totalCategories(categoryDtos.size())
                .build();
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Optional<Category> categoryOptional = this.categoryRepository.findById(id);
        if(categoryOptional.isEmpty()){
            throw new ApiException(HttpStatus.NOT_FOUND,"CATEGORY_NOT_FOUND","category not found with given id");
        }
        Category category =categoryOptional.get();
        CategoryResponse response =categoryMapper.toResponse(category);
        List<CategoryResponse> categoryResponseList;
        Optional<List<Category>> childCategory = categoryRepository.findByParentId(category.getId());
            if(childCategory.isPresent() && !childCategory.get().isEmpty()){
                categoryResponseList = categoryMapper.toDtoList(childCategory.get());
                response.setChildren(categoryResponseList);
            }
        return response;
    }

    @Override
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        Category category  = categoryRepository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND, "CATEGORY_NOT_FOUND", "Category not found"));
        Category newParent=null;
        if(request.getParentId()!=null){
            newParent = categoryRepository.findById(request.getParentId()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND, "PARENT_NOT_FOUND", "Parent Category not found"));
            validateCircularReference(category, newParent);
        }
        boolean exists = categoryRepository
                        .existsByNameIgnoreCaseAndParentIdAndIdNot(request.getName(), request.getParentId(), category.getId());
        if (exists) {
            throw new ApiException(HttpStatus.CONFLICT, "DUPLICATE_CATEGORY", "Category already exists");
        }
        category.setName(request.getName());
        category.setParent(newParent);
        int level = newParent == null ? 0 : newParent.getLevel() + 1;
        updateLevels(category, level);
        categoryRepository.save(category);
        CategoryAuditLog auditLog = CategoryAuditLog.builder()
                .categoryId(category.getId())
                .performedAt(LocalDateTime.now())
                .reason("Due to business")
                .build();
        categoryAuditLogRepository.save(auditLog);
        UpdateCategoryEventRequest eventRequest = UpdateCategoryEventRequest.builder()
                .categoryId(category.getId())
                .description(category.getDescription())
                .createdAt(LocalDateTime.now())
                .build();
        eventProducer.publishCategoryUpdateEvent(eventRequest);
        return CategoryResponse.builder()
                .categoryId(category.getId())
                .name(category.getName())
                .parentId(category.getParent().getId())
                .build();
    }

    @Override
    public void deleteCategory(Long id, DeleteCategoryRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND, "PARENT_NOT_FOUND", "Parent Category not found"));
        if(Boolean.FALSE.equals(category.getIsActive())){
            throw new ApiException(HttpStatus.NOT_FOUND,"ALREADY_INACTIVE","'This category is already inactive");
        }
        /* Salon service calling*/
        /* Booking service calling*/
        category.setIsActive(false);
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category);
        CategoryAuditLog auditLog = CategoryAuditLog.builder()
                .categoryId(id)
                .performedAt(LocalDateTime.now())
                .build();
        categoryAuditLogRepository.save(auditLog);
        UpdateCategoryEventRequest eventRequest = UpdateCategoryEventRequest.builder()
                .categoryId(id)
                .createdAt(LocalDateTime.now())
                .description(category.getDescription())
                .build();
        eventProducer.publishCategoryUpdateEvent(eventRequest);

    }

    @Override
    public CategoryResponse restoreCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND, "PARENT_NOT_FOUND", "Parent Category not found"));
        if(Boolean.TRUE.equals(category.getIsActive())){
            throw new ApiException(HttpStatus.NOT_FOUND,"ALREADY_ACTIVE","'This category is already active");
        }
        if(category.getParent()!=null){
            Category parentCategory = categoryRepository.findByParentId(category.getParent().getId()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND, "PARENT_NOT_FOUND", "Parent Category not found")).get(0);
            if(Boolean.FALSE.equals(parentCategory.getIsActive())){
                throw new ApiException(HttpStatus.BAD_REQUEST,"PARENT_INACTIVE","'Cannot restore: parent category is still inactive. Restore the parent first");
            }
        }
        CategoryAuditLog auditLog = CategoryAuditLog.builder()
                .categoryId(category.getId())
                .performedAt(LocalDateTime.now())
                .action("RESTORE").build();
        categoryAuditLogRepository.save(auditLog);
        CreateCategoryEventRequest eventRequest = CreateCategoryEventRequest.builder()
                .categoryId(category.getId())
                .createdAt(LocalDateTime.now())
                .description(category.getDescription())
                .build();
        eventProducer.publishCategoryRestoreEvent(eventRequest);
        return categoryMapper.toResponse(category);
    }

    @Override
    public CategoryResponse mergeCategory(Long id, MergeCategoryRequest request) {
        if(Objects.equals(id, request.getTargetCategoryId())){
            throw new ApiException(HttpStatus.BAD_REQUEST,"SELF_MERGE","");
        }
        Category sourceCategory = categoryRepository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND, "PARENT_NOT_FOUND", "Parent Category not found"));
        Category targetCategory = categoryRepository.findById(request.getTargetCategoryId()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND, "PARENT_NOT_FOUND", "Parent Category not found"));
        return null;
    }

    @Override
    public LookupCategoryResponse lookupCategory(List<String> idList) {
        List<Long> categoyIdList = idList.stream().map(Long::parseLong).toList();
        List<Category> categoryList = categoryRepository.findCategoryByListOfId(categoyIdList);
        List<LookupCategory> lookupCategoryList = categoryList.stream().map(categoryMapper::toLookUpCategory).toList();
        return LookupCategoryResponse.builder().lookupCategoryList(lookupCategoryList).build();
    }

    private void validateCircularReference(Category category,Category newParent){
        Category current = newParent;
        while (current!=null){
            if(current.getId().equals(category.getId())){
                throw new ApiException(
                        HttpStatus.BAD_REQUEST,
                        "CIRCULAR_REFERENCE",
                        "Category cannot be its own ancestor"
                );
            }
            current=current.getParent();
        }
    }


    private void updateLevels(Category category, int level) {
        category.setLevel(level);
    }

    private CategoryResponse mapToDto(Category category, Boolean includeCount) {
        CategoryResponse dto = CategoryResponse.builder()
                .categoryId(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .parentId(
                        category.getParent() != null
                                ? category.getParent().getId()
                                : null
                )
                .level(category.getLevel())
                .iconUrl(category.getIconUrl())
                .displayOrder(category.getDisplayOrder())
                .isFeatured(category.getIsFeatured())
                .children(new ArrayList<>())
                .build();
        if (Boolean.TRUE.equals(includeCount)) {
            dto.setSalonCount(
                    category.getSalonCount() != null
                            ? category.getSalonCount()
                            : 0
            );
            dto.setBookingCount(
                    category.getBookingCount() != null
                            ? category.getBookingCount()
                            : 0
            );
        }

        return dto;
    }

    private List<CategoryResponse> buildTree(List<CategoryResponse> categories) {
        Map<Long, CategoryResponse> map = categories.stream()
                .collect(Collectors.toMap(
                        CategoryResponse::getCategoryId,
                        Function.identity()));
        List<CategoryResponse> roots = new ArrayList<>();
        for (CategoryResponse dto : categories) {
            if (dto.getParentId() == null) {
                roots.add(dto);
            } else {
                CategoryResponse parent = map.get(dto.getParentId());
                if (parent != null) {
                    parent.getChildren().add(dto);
                }
            }
        }
        return roots;
    }



    private String getAccessToken(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }
}
