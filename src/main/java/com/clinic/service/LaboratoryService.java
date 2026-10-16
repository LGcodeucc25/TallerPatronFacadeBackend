package com.clinic.service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.clinic.model.ExamType;
import com.clinic.model.LabReference;
import com.clinic.model.LabResult;
import com.clinic.repository.LabResultRepository;

/**
 * Subsystem that simulates lab exams and stores their results.
 */
@Service
public class LaboratoryService {

    private final LabResultRepository labResultRepository;

    public LaboratoryService(LabResultRepository labResultRepository) {
        this.labResultRepository = labResultRepository;
    }

    public List<LabResult> requestExams(Long patientId, List<ExamType> exams) {
        List<LabResult> results = exams.stream()
                .distinct()
                .flatMap(exam -> exam.getReferences().stream()
                        .map(reference -> new LabResult(patientId, exam, reference, simulateValue(reference))))
                .toList();
        return labResultRepository.saveAll(results);
    }

    public List<LabResult> findByPatient(Long patientId) {
        return labResultRepository.findByPatientIdOrderByIdDesc(patientId);
    }

    /**
     * Random value between 80% of the minimum and 120% of the maximum reference.
     */
    private double simulateValue(LabReference reference) {
        double low = reference.min() * 0.8;
        double high = reference.max() * 1.2;
        double value = ThreadLocalRandom.current().nextDouble(low, high);
        return Math.round(value * 100.0) / 100.0;
    }
}
