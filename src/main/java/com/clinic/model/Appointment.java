package com.clinic.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private Long doctorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialty specialty;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    protected Appointment() {
    }

    public Appointment(Long patientId, Long doctorId, Specialty specialty, LocalDateTime dateTime) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.specialty = specialty;
        this.dateTime = dateTime;
        this.status = AppointmentStatus.SCHEDULED;
    }

    public void cancel() {
        this.status = AppointmentStatus.CANCELLED;
    }

    public Long getId() {
        return id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }
}
