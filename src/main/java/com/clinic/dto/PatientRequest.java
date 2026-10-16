package com.clinic.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record PatientRequest(
        @NotBlank(message = "documentNumber is required") String documentNumber,
        @NotBlank(message = "fullName is required") String fullName,
        @NotNull(message = "birthDate is required") @Past(message = "birthDate must be in the past") LocalDate birthDate,
        @Email(message = "email is not valid") String email,
        String phone,
        List<String> allergies) {
}
