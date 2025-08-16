package com.VentureBeez.OfferBeez.Admin;

public record MemberAssignmentDTO(
    Long membershipId,
    String empId,
    String name,
    String department,
    String project
) {}
