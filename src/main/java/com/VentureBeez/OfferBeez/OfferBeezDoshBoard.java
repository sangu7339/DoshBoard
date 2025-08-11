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
    private String newUser;   // Applicable / Not Applicable
    private String partner;   // Applicable / Not Applicable
    private String admin;     // Applicable / Not Applicable
    private String sales;     // Applicable / Not Applicable
    private int bugNumber;
    private String issue;
    private String severity;
    private String priority;

    private String inDev;
    private String inQA;
    private String reopen;
    private String verifyAndClosed;

    private String remarks;
}
