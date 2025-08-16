package com.VentureBeez.OfferBeez.Admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/search")
public class MemberSearchController {

    private final MemberSearchService memberSearchService;

    public MemberSearchController(MemberSearchService memberSearchService) {
        this.memberSearchService = memberSearchService;
    }

    @GetMapping("/by-empid")
    @PreAuthorize("hasRole('ADMIN')")
    public List<MemberAssignmentDTO> searchByEmpId(@RequestParam String empId) {
        return memberSearchService.findAssignmentsByEmpId(empId.trim());
    }
}
