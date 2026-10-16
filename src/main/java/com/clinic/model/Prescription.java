package com.clinic.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "prescription_id")
    private List<Medication> medications = new ArrayList<>();

    protected Prescription() {
    }

    public Prescription(Long patientId, List<Medication> medications) {
        this.patientId = patientId;
        this.date = LocalDate.now();
        this.medications.addAll(medications);
    }

    public Long getId() {
        return id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Medication> getMedications() {
        return medications;
    }
}
