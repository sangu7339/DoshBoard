package com.VentureBeez.OfferBeez.Admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddMemberRequest(
        @NotBlank String empId,
        @NotNull Long projectId,
        @NotNull Long departmentId
) {}