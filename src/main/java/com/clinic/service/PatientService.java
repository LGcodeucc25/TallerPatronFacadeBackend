package com.clinic.service;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.clinic.exception.BusinessException;
import com.clinic.exception.NotFoundException;
import com.clinic.model.Patient;
import com.clinic.repository.PatientRepository;

/**
 * Subsystem in charge of patient registration and lookup.
 */
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient register(String documentNumber, String fullName, LocalDate birthDate, String email, String phone) {
        String document = documentNumber.trim();
        if (patientRepository.existsByDocumentNumber(document)) {
            throw new BusinessException(HttpStatus.CONFLICT,
                    "A patient with document " + document + " already exists");
        }
        return patientRepository.save(new Patient(document, fullName.trim(), birthDate, email, phone));
    }

    public Patient findById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("Patient " + patientId + " not found"));
    }

    public Patient findByDocument(String documentNumber) {
        return patientRepository.findByDocumentNumber(documentNumber.trim())
                .orElseThrow(() -> new NotFoundException("Patient with document " + documentNumber + " not found"));
    }
}
