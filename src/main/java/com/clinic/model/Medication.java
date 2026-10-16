package com.clinic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Medication {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String dose;
    private int durationDays;

    protected Medication() {
    }

    public Medication(String name, String dose, int durationDays) {
        this.name = name;
        this.dose = dose;
        this.durationDays = durationDays;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDose() {
        return dose;
    }

    public int getDurationDays() {
        return durationDays;
    }
}
