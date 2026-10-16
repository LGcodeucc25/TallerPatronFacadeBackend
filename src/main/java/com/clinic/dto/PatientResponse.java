package com.clinic.dto;

import java.time.LocalDate;
import java.util.List;

import com.clinic.model.Patient;

public record PatientResponse(
        Long id,
        String documentNumber,
        String fullName,
        LocalDate birthDate,
        String email,
        String phone,
        List<String> allergies) {

    public static PatientResponse of(Patient patient, List<String> allergies) {
        return new PatientResponse(patient.getId(), patient.getDocumentNumber(), patient.getFullName(),
                patient.getBirthDate(), patient.getEmail(), patient.getPhone(), allergies);
    }
}
