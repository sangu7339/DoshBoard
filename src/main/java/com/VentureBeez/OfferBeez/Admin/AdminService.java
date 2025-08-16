package com.VentureBeez.OfferBeez.Admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepo;
    private final ProjectRepository projectRepo;
    private final DepartmentRepository deptRepo;
    private final MembershipRepository membershipRepo;

    public AdminService(UserRepository userRepo,
                        ProjectRepository projectRepo,
                        DepartmentRepository deptRepo,
                        MembershipRepository membershipRepo) {
        this.userRepo = userRepo;
        this.projectRepo = projectRepo;
        this.deptRepo = deptRepo;
        this.membershipRepo = membershipRepo;
    }

    @Transactional
    public Project createProject(Long adminUserId, CreateProjectRequest req) {
        projectRepo.findByName(req.name())
                .ifPresent(p -> { throw new IllegalArgumentException("Project name already exists"); });

        Project p = new Project();
        p.setName(req.name());
        p.setCreatedBy(adminUserId);
        return projectRepo.save(p);
    }

    @Transactional
    public Membership addMember(AddMemberRequest req) {
        User user = userRepo.findByEmpId(req.empId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "User with empId " + req.empId() + " not found"));

        Project project = projectRepo.findById(req.projectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        Department dept = deptRepo.findById(req.departmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        membershipRepo.findByUserIdAndProjectId(user.getId(), project.getId())
                .ifPresent(m -> { throw new IllegalArgumentException("User already assigned to this project"); });

        Membership m = new Membership();
        m.setUser(user);
        m.setProject(project);
        m.setDepartment(dept);

        return membershipRepo.save(m);
    }

    @Transactional
    public void removeMember(RemoveMemberRequest req) {
        User user = userRepo.findByEmpId(req.empId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "User with empId " + req.empId() + " not found"));

        Membership m = membershipRepo.findByUserIdAndProjectId(user.getId(), req.projectId())
                .orElseThrow(() -> new IllegalArgumentException("Membership not found"));

        membershipRepo.delete(m);
    }

    @Transactional
    public Membership reassignMember(ReassignMemberRequest req) {
        User user = userRepo.findByEmpId(req.empId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "User with empId " + req.empId() + " not found"));

        // Remove from old project if it exists
        membershipRepo.findByUserIdAndProjectId(user.getId(), req.fromProjectId())
                .ifPresent(membershipRepo::delete);

        Project toProject = projectRepo.findById(req.toProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Target project not found"));

        Department toDept = deptRepo.findById(req.toDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Target department not found"));

        membershipRepo.findByUserIdAndProjectId(user.getId(), req.toProjectId())
                .ifPresent(m -> { throw new IllegalArgumentException("Already assigned to target project"); });

        Membership m = new Membership();
        m.setUser(user);
        m.setProject(toProject);
        m.setDepartment(toDept);

        return membershipRepo.save(m);
    }

    public List<Membership> listMembers(Long projectIdOrNull) {
        if (projectIdOrNull == null) {
            return membershipRepo.findAll();
        }
        return membershipRepo.findByProjectId(projectIdOrNull);
    }
}
