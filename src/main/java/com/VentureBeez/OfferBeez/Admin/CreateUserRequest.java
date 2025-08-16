package com.VentureBeez.OfferBeez.Admin;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank String empId,
        @NotBlank String name
) {}
