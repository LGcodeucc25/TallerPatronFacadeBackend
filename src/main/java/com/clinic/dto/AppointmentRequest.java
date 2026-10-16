package com.clinic.dto;

import java.time.LocalDateTime;

import com.clinic.model.Specialty;

import jakarta.validation.constraints.NotNull;

public record AppointmentRequest(
        @NotNull(message = "patientId is required") Long patientId,
        @NotNull(message = "specialty is required") Specialty specialty,
        @NotNull(message = "doctorId is required") Long doctorId,
        @NotNull(message = "dateTime is required") LocalDateTime dateTime) {
}
