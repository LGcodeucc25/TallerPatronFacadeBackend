package com.clinic.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String documentNumber;

    @Column(nullable = false)
    private String fullName;

    private LocalDate birthDate;
    private String email;
    private String phone;

    protected Patient() {
    }

    public Patient(String documentNumber, String fullName, LocalDate birthDate, String email, String phone) {
        this.documentNumber = documentNumber;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
