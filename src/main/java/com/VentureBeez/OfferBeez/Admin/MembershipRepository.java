package com.VentureBeez.OfferBeez.Admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    Optional<Membership> findByUserIdAndProjectId(Long userId, Long projectId);

    List<Membership> findByProjectId(Long projectId);

    // For MemberSearchService
    @Query("SELECT m FROM Membership m " +
           "JOIN FETCH m.user u " +
           "JOIN FETCH m.department d " +
           "JOIN FETCH m.project p " +
           "WHERE u.empId = :empId")
    List<Membership> findAllByUserEmpId(String empId);
}
