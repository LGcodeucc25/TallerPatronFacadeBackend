package com.clinic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinic.dto.MedicationRequest;
import com.clinic.model.Medication;
import com.clinic.model.Prescription;
import com.clinic.repository.PrescriptionRepository;

/**
 * Subsystem in charge of issuing and listing prescriptions.
 */
@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public Prescription create(Long patientId, List<MedicationRequest> medications) {
        List<Medication> items = medications.stream()
                .map(item -> new Medication(item.name().trim(), item.dose().trim(), item.durationDays()))
                .toList();
        return prescriptionRepository.save(new Prescription(patientId, items));
    }

    public List<Prescription> findByPatient(Long patientId) {
        return prescriptionRepository.findByPatientIdOrderByIdDesc(patientId);
    }
}
