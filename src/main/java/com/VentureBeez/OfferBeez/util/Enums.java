package com.VentureBeez.OfferBeez.util;

public final class Enums {
    private Enums() {}

    // Overall stage used for dashboard counts & workflow
    public enum Stage { DEV, QA, RESOLVED }

    public enum Severity { S1, S2, S3 }   // Critical, Major, Minor
    public enum Priority { P1, P2, P3 }   // High, Medium, Low

    // Optional granular statuses per stage (for the sheet-like columns)
    public enum StageStatus { OPEN, PASS, FAIL, PENDING }
}
