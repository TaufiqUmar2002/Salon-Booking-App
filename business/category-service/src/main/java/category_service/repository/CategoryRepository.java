package category_service.repository;

import category_service.model.Category;
import category_service.model.CategoryAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>{

    Optional<Category> findCategoryByName(String  name);
    List<Category> findByIsActiveTrueOrderByDisplayOrderAsc();
    Optional<List<Category>> findByParentId(Long id);
    boolean existsByNameIgnoreCaseAndParentIdAndIdNot(String name, Long parentId, Long id);

    @Query("""
    SELECT A FROM Category A WHERE A.ID IN (:categoryIdList)
""")
    List<Category> findCategoryByListOfId(@Param("categoryIdList") List<Long> id);
}
