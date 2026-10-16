package com.clinic.dto;

public record ScheduledAppointmentResponse(AppointmentResponse appointment, String reminder) {
}
