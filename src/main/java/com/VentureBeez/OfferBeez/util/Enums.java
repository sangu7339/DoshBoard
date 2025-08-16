package com.VentureBeez.OfferBeez.util;

public final class Enums {
    private Enums() {}

    // Overall stage used for dashboard counts & workflow
    public enum Stage { DEV, QA, RESOLVED }

    public enum Severity { S1, S2, S3 }   // Critical, Major, Minor
    public enum Priority { P1, P2, P3 }   // High, Medium, Low

    // Optional granular statuses per stage (for the sheet-like columns)
    public enum StageStatus { OPEN, PASS, FAIL, PENDING }
    
    // New enum for Applicable/Not Applicable fields
    public enum Applicability { APPLICABLE, NOT_APPLICABLE }
    
    // Helper method to get display text
    public static String getApplicabilityDisplay(String value) {
        if ("APPLICABLE".equals(value)) return "Applicable";
        if ("NOT_APPLICABLE".equals(value)) return "Not Applicable";
        return value;
    }
}
