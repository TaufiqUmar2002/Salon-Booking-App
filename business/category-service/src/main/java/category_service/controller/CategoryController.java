package category_service.controller;

import category_service.serviceinterface.ICategoryService;
import com.umar.payload.request.category.CreateCategoryRequest;
import com.umar.payload.request.category.DeleteCategoryRequest;
import com.umar.payload.request.category.MergeCategoryRequest;
import com.umar.payload.request.category.UpdateCategoryRequest;
import com.umar.payload.response.category.CategoryResponse;
import com.umar.payload.response.category.CategoryResponseList;
import com.umar.payload.response.category.LookupCategoryResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {


    private final ICategoryService categoryService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request, HttpServletRequest servletRequest){
        CategoryResponse categoryResponse =categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<CategoryResponseList> getAllCategories(@RequestParam(defaultValue = "flat") String format,
                                                                 @RequestParam(defaultValue = "false") Boolean topLevelOnly,
                                                                 @RequestParam(defaultValue = "false") Boolean featuredOnly,
                                                                 @RequestParam(defaultValue = "false") Boolean includeCount  ){
        return ResponseEntity.status(HttpStatus.OK).body( categoryService.getAllCategory(format,topLevelOnly,featuredOnly,includeCount));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SALON_OWNER')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable("id") Long id){
        CategoryResponse categoryResponse = this.categoryService.getCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(categoryResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable("id") Long id ,@Valid@RequestBody UpdateCategoryRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.updateCategory(id,request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id, @RequestBody DeleteCategoryRequest request){
        this.categoryService.deleteCategory(id,request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/restore")
    public ResponseEntity<CategoryResponse> restoreCategory(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.restoreCategory(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/merge")
    public ResponseEntity<CategoryResponse> mergeCategory(@PathVariable("id") Long id,@RequestBody MergeCategoryRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.mergeCategory(id,request));
    }

    @GetMapping("/internal/lookup")
    public ResponseEntity<LookupCategoryResponse> lookupCategory(@RequestParam List<String> idList){
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.lookupCategory(idList));
    }


}
