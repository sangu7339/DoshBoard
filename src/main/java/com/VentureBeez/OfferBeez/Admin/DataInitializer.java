package com.VentureBeez.OfferBeez.Admin;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seedDepartments(DepartmentRepository deptRepo) {
        return args -> {
            List<String> names = List.of("Test", "Development", "DevOps", "Management", "Client");
            for (String n : names) {
                deptRepo.findByName(n).orElseGet(() -> {
                    Department d = new Department();
                    d.setName(n);
                    return deptRepo.save(d);
                });
            }
        };
    }
}
