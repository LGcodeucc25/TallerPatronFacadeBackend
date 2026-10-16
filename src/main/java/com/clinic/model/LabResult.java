package com.clinic.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class LabResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamType examType;

    private String parameter;

    @Column(name = "result_value")
    private double value;

    private String unit;
    private double minReference;
    private double maxReference;
    private boolean withinRange;

    protected LabResult() {
    }

    public LabResult(Long patientId, ExamType examType, LabReference reference, double value) {
        this.patientId = patientId;
        this.date = LocalDate.now();
        this.examType = examType;
        this.parameter = reference.parameter();
        this.value = value;
        this.unit = reference.unit();
        this.minReference = reference.min();
        this.maxReference = reference.max();
        this.withinRange = value >= reference.min() && value <= reference.max();
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

    public ExamType getExamType() {
        return examType;
    }

    public String getParameter() {
        return parameter;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public double getMinReference() {
        return minReference;
    }

    public double getMaxReference() {
        return maxReference;
    }

    public boolean isWithinRange() {
        return withinRange;
    }
}
