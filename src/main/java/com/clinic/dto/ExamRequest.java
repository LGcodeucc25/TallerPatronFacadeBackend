package com.clinic.dto;

import java.util.List;

import com.clinic.model.ExamType;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ExamRequest(
        @NotNull(message = "patientId is required") Long patientId,
        @NotEmpty(message = "exams must not be empty") List<@NotNull ExamType> exams) {
}
