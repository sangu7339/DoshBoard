package com.VentureBeez.OfferBeez.Admin;


import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
  private final AdminService adminService;
  private final ProjectRepository projectRepo;
  private final DepartmentRepository deptRepo;

  public AdminController(AdminService adminService, ProjectRepository projectRepo, DepartmentRepository deptRepo) {
    this.adminService = adminService;
    this.projectRepo = projectRepo;
    this.deptRepo = deptRepo;
  }

  @PostMapping("/createProject")
  @PreAuthorize("hasRole('ADMIN')")
  public Project createProject(@Valid @RequestBody CreateProjectRequest req, Authentication auth) {
    // map authenticated admin (in-memory) to a constant id for auditing
    return adminService.createProject(1L, req);
  }

  @GetMapping("/projects")
  @PreAuthorize("hasRole('ADMIN')")
  public List<Project> projects() { return projectRepo.findAll(); }

  @GetMapping("/departments")
  @PreAuthorize("hasRole('ADMIN')")
  public List<Department> departments() { return deptRepo.findAll(); }

  @PostMapping("/addMember")
  @PreAuthorize("hasRole('ADMIN')")
  public Membership addMember(@Valid @RequestBody AddMemberRequest req) {
    return adminService.addMember(req);
  }

  @PostMapping("/removeMember")
  @PreAuthorize("hasRole('ADMIN')")
  public void removeMember(@Valid @RequestBody RemoveMemberRequest req) {
    adminService.removeMember(req);
  }

  @PostMapping("/reassignMember")
  @PreAuthorize("hasRole('ADMIN')")
  public Membership reassignMember(@Valid @RequestBody ReassignMemberRequest req) {
    return adminService.reassignMember(req);
  }

  @GetMapping("/listMembers")
  @PreAuthorize("hasRole('ADMIN')")
  public List<Membership> listMembers(@RequestParam(required=false) Long projectId) {
    return adminService.listMembers(projectId);
  }
}
