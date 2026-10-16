package com.clinic.dto;

import java.util.List;

import com.clinic.model.Consultation;
import com.clinic.model.LabResult;
import com.clinic.model.Prescription;

public record FullHistoryResponse(
        PatientResponse patient,
        List<AppointmentResponse> appointments,
        List<Consultation> consultations,
        List<Prescription> prescriptions,
        List<LabResult> labResults) {
}
