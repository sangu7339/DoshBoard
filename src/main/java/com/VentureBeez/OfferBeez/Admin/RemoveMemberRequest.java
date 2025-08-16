package com.VentureBeez.OfferBeez.Admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RemoveMemberRequest(
        @NotBlank String empId,     // Employee ID, e.g. "emp001"
        @NotNull Long projectId     // Numeric DB ID of the project
) {}
