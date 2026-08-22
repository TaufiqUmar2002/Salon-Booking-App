package com.umar.repository;

import com.umar.model.EodBodRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EodBodRunRepository extends JpaRepository<EodBodRun, Long> {
}
