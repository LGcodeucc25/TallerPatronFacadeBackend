package com.clinic.dto;

import java.time.LocalDateTime;

import com.clinic.model.AppointmentStatus;
import com.clinic.model.Specialty;
import com.fasterxml.jackson.annotation.JsonFormat;

public record AppointmentResponse(
        Long id,
        String doctorName,
        Specialty specialty,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime dateTime,
        AppointmentStatus status) {
}
