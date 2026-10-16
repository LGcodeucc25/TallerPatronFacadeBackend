package com.clinic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.model.LabResult;

public interface LabResultRepository extends JpaRepository<LabResult, Long> {

    List<LabResult> findByPatientIdOrderByIdDesc(Long patientId);
}
