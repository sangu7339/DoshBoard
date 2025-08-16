package com.VentureBeez.OfferBeez.Admin;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmpId(String empId);
    Optional<User> findByName(String name);

}
