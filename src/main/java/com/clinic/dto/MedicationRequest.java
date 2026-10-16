package com.clinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record MedicationRequest(
        @NotBlank(message = "medication name is required") String name,
        @NotBlank(message = "medication dose is required") String dose,
        @Positive(message = "durationDays must be greater than 0") int durationDays) {
}
