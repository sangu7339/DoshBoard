package com.VentureBeez.OfferBeez.Admin;

import jakarta.validation.constraints.NotBlank;

public record CreateProjectRequest(@NotBlank String name) {}