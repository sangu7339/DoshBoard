package com.VentureBeez.OfferBeez.Admin;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long id;

    @Column(name = "emp_id", nullable = false, unique = true)
    private String empId; // e.g. emp001

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String role; // ADMIN or USER

    @Column(nullable = false)
    private String name; // display name
}
