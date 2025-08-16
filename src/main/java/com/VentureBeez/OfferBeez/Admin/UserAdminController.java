package com.VentureBeez.OfferBeez.Admin;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
public class UserAdminController {

    private final UserRepository userRepo;

    public UserAdminController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public User createUser(@RequestBody CreateUserRequest req) {

        // ✅ Check if Employee Name already exists
        userRepo.findByName(req.name())
            .ifPresent(u -> {
                throw new ResponseStatusException(
                    HttpStatus.CONFLICT,   // 409
                    "Employee Name already exists"
                );
            });

        User u = new User();
        u.setEmpId(req.empId());           // keep empId unique in DB
        u.setName(req.name());             // display name
        u.setRole("USER");
        u.setPasswordHash("N/A"); // Not used for login
        return userRepo.save(u);
    }
}
