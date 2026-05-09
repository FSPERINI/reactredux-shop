package com.ecommerce.api.repository;

import com.ecommerce.api.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    List<Promotion> findByActiveTrue();

    @Query("SELECT p FROM Promotion p WHERE p.active = false AND p.startDate <= :now AND p.endDate > :now")
    List<Promotion> findInactiveToActivate(@Param("now") LocalDateTime now);

    @Query("SELECT p FROM Promotion p WHERE p.active = true AND p.endDate <= :now")
    List<Promotion> findActiveToDeactivate(@Param("now") LocalDateTime now);
}
