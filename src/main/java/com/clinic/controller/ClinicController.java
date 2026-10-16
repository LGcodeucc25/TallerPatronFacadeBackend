package com.clinic.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.dto.AppointmentRequest;
import com.clinic.dto.DoctorResponse;
import com.clinic.dto.ExamRequest;
import com.clinic.dto.FullHistoryResponse;
import com.clinic.dto.PatientRequest;
import com.clinic.dto.PatientResponse;
import com.clinic.dto.PrescriptionRequest;
import com.clinic.dto.ScheduledAppointmentResponse;
import com.clinic.facade.ClinicFacade;
import com.clinic.model.LabResult;
import com.clinic.model.Prescription;
import com.clinic.model.Specialty;

import jakarta.validation.Valid;

/**
 * REST entry point. It only talks to the ClinicFacade, never to the subsystems.
 */
@RestController
@RequestMapping("/api/clinica")
public class ClinicController {

    private final ClinicFacade clinicFacade;

    public ClinicController(ClinicFacade clinicFacade) {
        this.clinicFacade = clinicFacade;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }

    @PostMapping("/paciente")
    @ResponseStatus(HttpStatus.CREATED)
    public PatientResponse registerPatient(@Valid @RequestBody PatientRequest request) {
        return clinicFacade.registerPatient(request);
    }

    @GetMapping("/paciente/{documentNumber}")
    public PatientResponse findPatient(@PathVariable String documentNumber) {
        return clinicFacade.findPatientByDocument(documentNumber);
    }

    @GetMapping("/medicos")
    public List<DoctorResponse> findDoctors(@RequestParam(name = "especialidad", required = false) Specialty specialty) {
        return clinicFacade.findDoctors(specialty);
    }

    @PostMapping("/cita")
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduledAppointmentResponse scheduleAppointment(@Valid @RequestBody AppointmentRequest request) {
        return clinicFacade.scheduleAppointment(request.patientId(), request.specialty(), request.dateTime(),
                request.doctorId());
    }

    @GetMapping("/historia/{patientId}")
    public FullHistoryResponse getFullHistory(@PathVariable Long patientId) {
        return clinicFacade.getFullHistory(patientId);
    }

    @PostMapping("/prescripcion")
    @ResponseStatus(HttpStatus.CREATED)
    public Prescription generatePrescription(@Valid @RequestBody PrescriptionRequest request) {
        return clinicFacade.generatePrescription(request.patientId(), request.medications());
    }

    @PostMapping("/laboratorio")
    @ResponseStatus(HttpStatus.CREATED)
    public List<LabResult> requestExams(@Valid @RequestBody ExamRequest request) {
        return clinicFacade.requestExams(request.patientId(), request.exams());
    }
}
