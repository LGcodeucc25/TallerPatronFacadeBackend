package com.clinic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinic.model.Allergy;
import com.clinic.model.Consultation;
import com.clinic.repository.AllergyRepository;
import com.clinic.repository.ConsultationRepository;

/**
 * Subsystem in charge of consultations and known allergies of each patient.
 */
@Service
public class MedicalHistoryService {

    private final ConsultationRepository consultationRepository;
    private final AllergyRepository allergyRepository;

    public MedicalHistoryService(ConsultationRepository consultationRepository, AllergyRepository allergyRepository) {
        this.consultationRepository = consultationRepository;
        this.allergyRepository = allergyRepository;
    }

    public Consultation recordConsultation(Long patientId, String doctorName, String diagnosis, String notes) {
        return consultationRepository.save(new Consultation(patientId, doctorName, diagnosis, notes));
    }

    public List<Consultation> findConsultations(Long patientId) {
        return consultationRepository.findByPatientIdOrderByIdDesc(patientId);
    }

    public List<String> recordAllergies(Long patientId, List<String> allergies) {
        if (allergies == null) {
            return List.of();
        }
        List<Allergy> entries = allergies.stream()
                .filter(name -> name != null && !name.isBlank())
                .map(name -> new Allergy(patientId, name.trim()))
                .toList();
        return allergyRepository.saveAll(entries).stream().map(Allergy::getName).toList();
    }

    public List<String> getAllergies(Long patientId) {
        return allergyRepository.findByPatientId(patientId).stream().map(Allergy::getName).toList();
    }
}
