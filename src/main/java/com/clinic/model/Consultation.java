package com.clinic.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private LocalDate date;

    private String doctorName;
    private String diagnosis;

    @Column(length = 1000)
    private String notes;

    protected Consultation() {
    }

    public Consultation(Long patientId, String doctorName, String diagnosis, String notes) {
        this.patientId = patientId;
        this.date = LocalDate.now();
        this.doctorName = doctorName;
        this.diagnosis = diagnosis;
        this.notes = notes;
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

    public String getDoctorName() {
        return doctorName;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getNotes() {
        return notes;
    }
}
