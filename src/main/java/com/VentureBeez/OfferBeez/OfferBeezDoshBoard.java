package com.VentureBeez.OfferBeez;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class OfferBeezDoshBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String project;

    // Applicable / Not Applicable
    private String newUser;
    private String partner;
    private String admin;
    private String sales;

    // Bug details
    private String bugNumber;   // Changed from int to String
    private String issue;
    private String severity;    // S1, S2, S3
    private String priority;    // P1, P2, P3

    // Status tracking
    private String status;      // "In Dev", "In QA"
    private boolean live;       // true = live bug, false = previous bug

    private String inDev;       // open / pass / fail
    private String inQA;        // open / pass / fail
    private String reopen;      // yes / no
    private String verifyAndClosed; // pass / fail / pending

    private String remarks;
}
