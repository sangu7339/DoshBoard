package com.VentureBeez.OfferBeez;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppRepository extends JpaRepository<OfferBeezDoshBoard, Integer> {

    // Lists
    List<OfferBeezDoshBoard> findByProject(String project);
    List<OfferBeezDoshBoard> findBySeverity(String severity);
    List<OfferBeezDoshBoard> findByPriority(String priority);
    List<OfferBeezDoshBoard> findByLive(boolean live);
    List<OfferBeezDoshBoard> findByStatusIgnoreCase(String status);

    // Case-insensitive list variants
    List<OfferBeezDoshBoard> findByProjectIgnoreCase(String project);
    List<OfferBeezDoshBoard> findBySeverityIgnoreCase(String severity);
    List<OfferBeezDoshBoard> findByPriorityIgnoreCase(String priority);

    // Derived counts
    long countByInDevIgnoreCase(String inDev);
    long countByInQAIgnoreCase(String inQA);
    long countBySeverityIgnoreCase(String severity);
    long countByPriorityIgnoreCase(String priority);

    // FIX: Use @Query here because verifyAndClosed contains "And" and breaks parsing
    @Query("SELECT COUNT(o) FROM OfferBeezDoshBoard o WHERE LOWER(o.verifyAndClosed) = LOWER(:state)")
    long countByVerifyAndClosedIgnoreCase(@Param("state") String verifyAndClosed);

    long countByLive(boolean live);

    // FIX: Same for combined query
    @Query("SELECT COUNT(o) FROM OfferBeezDoshBoard o WHERE o.live = :live AND LOWER(o.verifyAndClosed) = LOWER(:state)")
    long countByLiveAndVerifyAndClosedIgnoreCase(@Param("live") boolean live, @Param("state") String verifyAndClosed);

    // JPQL optional methods
    @Query("SELECT COUNT(o) FROM OfferBeezDoshBoard o WHERE LOWER(o.inQA) = LOWER(:status)")
    Long countByQAStatus(@Param("status") String status);

    @Query("SELECT COUNT(o) FROM OfferBeezDoshBoard o WHERE LOWER(o.severity) = LOWER(:severity)")
    Long countBySeverityJPQL(@Param("severity") String severity);

    @Query("SELECT COUNT(o) FROM OfferBeezDoshBoard o WHERE LOWER(o.priority) = LOWER(:priority)")
    Long countByPriorityJPQL(@Param("priority") String priority);

    @Query("SELECT COUNT(o) FROM OfferBeezDoshBoard o WHERE o.live = :live")
    Long countByLiveStatusJPQL(@Param("live") boolean live);

    @Query("SELECT COUNT(o) FROM OfferBeezDoshBoard o WHERE o.live = :live AND LOWER(o.verifyAndClosed) = LOWER(:state)")
    Long countByLiveAndVerifyJPQL(@Param("live") boolean live, @Param("state") String state);
}
