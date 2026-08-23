package com.umar.service_offering.repository;

import com.umar.service_offering.model.CategoryPriceBenchmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryPriceBenchmarkRepository extends JpaRepository<CategoryPriceBenchmark,Long> {

    @Query("SELECT cpb FROM CategoryPriceBenchmark cpb WHERE cpb.city = :city AND cpb.categoryId = :categoryId")
    Optional<CategoryPriceBenchmark> findByCityAndCategoryId(String city, Long categoryId);
}
