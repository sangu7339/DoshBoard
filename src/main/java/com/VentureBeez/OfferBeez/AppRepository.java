package com.VentureBeez.OfferBeez;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppRepository extends JpaRepository<OfferBeezDoshBoard, Integer> {
    
    // Custom queries for better performance
    @Query("SELECT COUNT(o) FROM OfferBeezDoshBoard o WHERE o.inDev = :status")
    Long countByDevStatus(@Param("status") String status);
    
    @Query("SELECT COUNT(o) FROM OfferBeezDoshBoard o WHERE o.inQA = :status")
    Long countByQAStatus(@Param("status") String status);
    
    @Query("SELECT COUNT(o) FROM OfferBeezDoshBoard o WHERE o.severity = :severity")
    Long countBySeverity(@Param("severity") String severity);
    
    @Query("SELECT COUNT(o) FROM OfferBeezDoshBoard o WHERE o.priority = :priority")
    Long countByPriority(@Param("priority") String priority);
    
    @Query("SELECT COUNT(o) FROM OfferBeezDoshBoard o WHERE o.live = :live")
    Long countByLiveStatus(@Param("live") boolean live);
    
    List<OfferBeezDoshBoard> findByProject(String project);
    List<OfferBeezDoshBoard> findBySeverity(String severity);
    List<OfferBeezDoshBoard> findByPriority(String priority);
    List<OfferBeezDoshBoard> findByLive(boolean live);
}
