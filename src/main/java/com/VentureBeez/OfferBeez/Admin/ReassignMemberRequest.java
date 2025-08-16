package com.VentureBeez.OfferBeez.Admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReassignMemberRequest(
        @NotBlank String empId,           // employee ID, e.g., "emp001"
        @NotNull Long fromProjectId,
        @NotNull Long toProjectId,
        @NotNull Long toDepartmentId
) {}
