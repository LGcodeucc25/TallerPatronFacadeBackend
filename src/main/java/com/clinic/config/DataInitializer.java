package com.clinic.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.clinic.model.Doctor;
import com.clinic.model.Specialty;
import com.clinic.repository.DoctorRepository;

/**
 * Inserts the five predefined doctors only when the doctor table is empty.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final DoctorRepository doctorRepository;

    public DataInitializer(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public void run(String... args) {
        if (doctorRepository.count() > 0) {
            return;
        }
        doctorRepository.saveAll(List.of(
                new Doctor("Laura Gomez", Specialty.GENERAL_MEDICINE),
                new Doctor("Carlos Ramirez", Specialty.GENERAL_MEDICINE),
                new Doctor("Ana Torres", Specialty.CARDIOLOGY),
                new Doctor("Miguel Herrera", Specialty.CARDIOLOGY),
                new Doctor("Sofia Martinez", Specialty.PEDIATRICS)));
    }
}
