package com.VentureBeez.OfferBeez.Admin;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MemberSearchService {

    private final MembershipRepository membershipRepo;
    private final UserRepository userRepo;

    public MemberSearchService(MembershipRepository membershipRepo, UserRepository userRepo) {
        this.membershipRepo = membershipRepo;
        this.userRepo = userRepo;
    }

    public List<MemberAssignmentDTO> findAssignmentsByEmpId(String empId) {
        // Throws if user doesn't exist
        userRepo.findByEmpId(empId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User with empId " + empId + " not found"));

        return membershipRepo.findAllByUserEmpId(empId).stream()
                .map(m -> new MemberAssignmentDTO(
                        m.getId(),
                        m.getUser().getEmpId(),
                        m.getUser().getName(),
                        m.getDepartment().getName(),
                        m.getProject().getName()
                ))
                .toList();
    }
}
