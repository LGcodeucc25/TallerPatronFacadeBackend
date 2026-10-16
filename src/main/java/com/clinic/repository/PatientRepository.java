package com.clinic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByDocumentNumber(String documentNumber);

    boolean existsByDocumentNumber(String documentNumber);
}
