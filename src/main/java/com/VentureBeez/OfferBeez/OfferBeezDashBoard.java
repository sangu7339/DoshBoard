package com.VentureBeez.OfferBeez;

import jakarta.persistence.*;
import lombok.Data;
import com.VentureBeez.OfferBeez.util.Enums;

@Entity
@Data
public class OfferBeezDashBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String project;
    private String bugNumber;
    private String issue;
    
    // Use enum types instead of String
    @Enumerated(EnumType.STRING)
    private Enums.Severity severity;    // S1, S2, S3
    
    @Enumerated(EnumType.STRING)
    private Enums.Priority priority;    // P1, P2, P3
    
    @Enumerated(EnumType.STRING)
    private Enums.Stage status;         // DEV, QA, RESOLVED
    
    private boolean live;       // true = live bug, false = resolved
    private String remarks;
    
    // Helper method to get status as string for display
    public String getStatusDisplay() {
        switch (status) {
            case DEV: return "In Dev";
            case QA: return "In QA";
            case RESOLVED: return "Resolved";
            default: return status.toString();
        }
    }
}
