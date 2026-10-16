package com.clinic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.model.Doctor;
import com.clinic.model.Specialty;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findBySpecialty(Specialty specialty);
}
