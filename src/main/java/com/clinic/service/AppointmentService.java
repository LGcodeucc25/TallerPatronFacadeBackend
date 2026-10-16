package com.clinic.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clinic.dto.AppointmentResponse;
import com.clinic.dto.DoctorResponse;
import com.clinic.exception.BusinessException;
import com.clinic.exception.NotFoundException;
import com.clinic.model.Appointment;
import com.clinic.model.AppointmentStatus;
import com.clinic.model.Doctor;
import com.clinic.model.Specialty;
import com.clinic.repository.AppointmentRepository;
import com.clinic.repository.DoctorRepository;

/**
 * Subsystem in charge of doctors' agendas and appointment booking.
 */
@Service
public class AppointmentService {

    private static final int BUSINESS_DAYS = 5;
    private static final int FIRST_HOUR = 8;
    private static final int LAST_HOUR = 11;
    private static final DateTimeFormatter SLOT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter REMINDER_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm");

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public List<DoctorResponse> findDoctorsBySpecialty(Specialty specialty) {
        List<Doctor> doctors = specialty == null
                ? doctorRepository.findAll()
                : doctorRepository.findBySpecialty(specialty);
        return doctors.stream()
                .map(doctor -> new DoctorResponse(doctor.getId(), doctor.getFullName(), doctor.getSpecialty(),
                        availableSlots(doctor.getId()).stream().map(SLOT_FORMAT::format).toList()))
                .toList();
    }

    public List<LocalDateTime> availableSlots(Long doctorId) {
        Set<LocalDateTime> booked = appointmentRepository
                .findByDoctorIdAndStatus(doctorId, AppointmentStatus.SCHEDULED).stream()
                .map(Appointment::getDateTime)
                .collect(Collectors.toSet());
        return candidateSlots().stream().filter(slot -> !booked.contains(slot)).toList();
    }

    @Transactional
    public AppointmentResponse book(Long patientId, Long doctorId, Specialty specialty, LocalDateTime dateTime) {
        Doctor doctor = findDoctor(doctorId);
        if (doctor.getSpecialty() != specialty) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    "Doctor " + doctor.getFullName() + " does not practice " + specialty);
        }
        LocalDateTime slot = dateTime.truncatedTo(ChronoUnit.MINUTES);
        if (!candidateSlots().contains(slot)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    "Appointments are available on the next 5 business days from 08:00 to 11:00 every hour");
        }
        if (appointmentRepository.existsByDoctorIdAndDateTimeAndStatus(doctorId, slot, AppointmentStatus.SCHEDULED)) {
            throw new BusinessException(HttpStatus.CONFLICT, "The selected time slot is already booked");
        }
        Appointment appointment = appointmentRepository.save(new Appointment(patientId, doctorId, specialty, slot));
        return toResponse(appointment, doctor);
    }

    @Transactional
    public AppointmentResponse cancel(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment " + appointmentId + " not found"));
        appointment.cancel();
        return toResponse(appointmentRepository.save(appointment), findDoctor(appointment.getDoctorId()));
    }

    public List<AppointmentResponse> findByPatient(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByDateTimeAsc(patientId).stream()
                .map(appointment -> toResponse(appointment, findDoctor(appointment.getDoctorId())))
                .toList();
    }

    public String buildReminder(AppointmentResponse appointment) {
        return "Reminder: your appointment with Dr. " + appointment.doctorName()
                + " (" + appointment.specialty() + ") is on " + REMINDER_FORMAT.format(appointment.dateTime()) + ".";
    }

    private Doctor findDoctor(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("Doctor " + doctorId + " not found"));
    }

    private List<LocalDateTime> candidateSlots() {
        List<LocalDateTime> slots = new ArrayList<>();
        LocalDate day = LocalDate.now();
        int businessDays = 0;
        while (businessDays < BUSINESS_DAYS) {
            day = day.plusDays(1);
            if (day.getDayOfWeek() == DayOfWeek.SATURDAY || day.getDayOfWeek() == DayOfWeek.SUNDAY) {
                continue;
            }
            businessDays++;
            for (int hour = FIRST_HOUR; hour <= LAST_HOUR; hour++) {
                slots.add(day.atTime(hour, 0));
            }
        }
        return slots;
    }

    private AppointmentResponse toResponse(Appointment appointment, Doctor doctor) {
        return new AppointmentResponse(appointment.getId(), doctor.getFullName(), appointment.getSpecialty(),
                appointment.getDateTime(), appointment.getStatus());
    }
}
