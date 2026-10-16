package com.clinic.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinic.model.Appointment;
import com.clinic.model.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientIdOrderByDateTimeAsc(Long patientId);

    List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    boolean existsByDoctorIdAndDateTimeAndStatus(Long doctorId, LocalDateTime dateTime, AppointmentStatus status);
}
