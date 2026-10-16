package com.clinic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.model.Consultation;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    List<Consultation> findByPatientIdOrderByIdDesc(Long patientId);
}
