package com.VentureBeez.OfferBeez;

import jakarta.persistence.*;
import lombok.Data;
import com.VentureBeez.OfferBeez.util.Enums;

@Entity
@Data
@Table(name = "offer_beez_dash_board")
public class OfferBeezDashBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "project")
    private String project;
    
    @Column(name = "bug_number")
    private String bugNumber;
    
    @Column(name = "issue", columnDefinition = "TEXT")
    private String issue;
    
    // Use enum types with proper mapping
    @Enumerated(EnumType.STRING)
    @Column(name = "severity")
    private Enums.Severity severity;    // S1, S2, S3
    
    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Enums.Priority priority;    // P1, P2, P3
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Enums.Stage status;         // DEV, QA, RESOLVED
    
    @Column(name = "live")
    private boolean live;       // true = live bug, false = resolved
    
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
    // New fields - Applicable / Not Applicable
    @Column(name = "new_user")
    private String newUser;     // APPLICABLE / NOT_APPLICABLE
    
    @Column(name = "partner")
    private String partner;     // APPLICABLE / NOT_APPLICABLE
    
    @Column(name = "admin")
    private String admin;       // APPLICABLE / NOT_APPLICABLE
    
    @Column(name = "sales")
    private String sales;       // APPLICABLE / NOT_APPLICABLE
    
    // Helper method to get status as string for display
    public String getStatusDisplay() {
        if (status == null) return "Unknown";
        switch (status) {
            case DEV: return "In Dev";
            case QA: return "In QA";
            case RESOLVED: return "Resolved";
            default: return status.toString();
        }
    }
    
    // Helper methods for debugging
    @Override
    public String toString() {
        return "OfferBeezDashBoard{" +
                "id=" + id +
                ", project='" + project + '\'' +
                ", bugNumber='" + bugNumber + '\'' +
                ", severity=" + severity +
                ", priority=" + priority +
                ", status=" + status +
                ", admin='" + admin + '\'' +
                ", newUser='" + newUser + '\'' +
                ", sales='" + sales + '\'' +
                ", partner='" + partner + '\'' +
                '}';
    }
}
