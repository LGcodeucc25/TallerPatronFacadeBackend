package com.clinic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.model.Allergy;

public interface AllergyRepository extends JpaRepository<Allergy, Long> {

    List<Allergy> findByPatientId(Long patientId);
}
