package com.clinic.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PrescriptionRequest(
        @NotNull(message = "patientId is required") Long patientId,
        @NotEmpty(message = "medications must not be empty") List<@Valid @NotNull MedicationRequest> medications) {
}
