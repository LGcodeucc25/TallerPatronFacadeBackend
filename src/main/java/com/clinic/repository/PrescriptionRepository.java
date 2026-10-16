package com.clinic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.model.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByPatientIdOrderByIdDesc(Long patientId);
}
