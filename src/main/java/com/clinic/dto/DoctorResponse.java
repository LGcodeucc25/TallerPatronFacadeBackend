package com.clinic.dto;

import java.util.List;

import com.clinic.model.Specialty;

public record DoctorResponse(Long id, String fullName, Specialty specialty, List<String> availableSlots) {
}
