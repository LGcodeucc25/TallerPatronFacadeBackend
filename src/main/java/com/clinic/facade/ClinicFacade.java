package com.clinic.facade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.clinic.dto.AppointmentResponse;
import com.clinic.dto.DoctorResponse;
import com.clinic.dto.FullHistoryResponse;
import com.clinic.dto.MedicationRequest;
import com.clinic.dto.PatientRequest;
import com.clinic.dto.PatientResponse;
import com.clinic.dto.ScheduledAppointmentResponse;
import com.clinic.exception.BusinessException;
import com.clinic.model.ExamType;
import com.clinic.model.LabResult;
import com.clinic.model.Patient;
import com.clinic.model.Prescription;
import com.clinic.model.Specialty;
import com.clinic.service.AppointmentService;
import com.clinic.service.LaboratoryService;
import com.clinic.service.MedicalHistoryService;
import com.clinic.service.PatientService;
import com.clinic.service.PrescriptionService;

/**
 * Single entry point to the clinic. It coordinates the five subsystems so that
 * clients never deal with them directly, and the subsystems never know each other.
 */
@Component
public class ClinicFacade {

    private static final String CLINIC_STAFF = "Clinic staff";
    private static final String LABORATORY = "Laboratory";

    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final MedicalHistoryService medicalHistoryService;
    private final PrescriptionService prescriptionService;
    private final LaboratoryService laboratoryService;

    public ClinicFacade(PatientService patientService, AppointmentService appointmentService,
            MedicalHistoryService medicalHistoryService, PrescriptionService prescriptionService,
            LaboratoryService laboratoryService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.medicalHistoryService = medicalHistoryService;
        this.prescriptionService = prescriptionService;
        this.laboratoryService = laboratoryService;
    }

    /** Patient + MedicalHistory: stores the patient and their known allergies. */
    @Transactional
    public PatientResponse registerPatient(PatientRequest request) {
        Patient patient = patientService.register(request.documentNumber(), request.fullName(),
                request.birthDate(), request.email(), request.phone());
        List<String> allergies = medicalHistoryService.recordAllergies(patient.getId(), request.allergies());
        return PatientResponse.of(patient, allergies);
    }

    /** Patient + MedicalHistory: used by the frontend login. */
    @Transactional(readOnly = true)
    public PatientResponse findPatientByDocument(String documentNumber) {
        Patient patient = patientService.findByDocument(documentNumber);
        return PatientResponse.of(patient, medicalHistoryService.getAllergies(patient.getId()));
    }

    /** Appointment: doctors of a specialty with their free slots. */
    @Transactional(readOnly = true)
    public List<DoctorResponse> findDoctors(Specialty specialty) {
        return appointmentService.findDoctorsBySpecialty(specialty);
    }

    /** Patient + Appointment + MedicalHistory. */
    @Transactional
    public ScheduledAppointmentResponse scheduleAppointment(Long patientId, Specialty specialty,
            LocalDateTime dateTime, Long doctorId) {
        patientService.findById(patientId);
        AppointmentResponse appointment = appointmentService.book(patientId, doctorId, specialty, dateTime);
        String reminder = appointmentService.buildReminder(appointment);
        medicalHistoryService.recordConsultation(patientId, appointment.doctorName(),
                "Scheduled appointment", reminder);
        return new ScheduledAppointmentResponse(appointment, reminder);
    }

    /** Patient + Appointment + MedicalHistory + Prescription + Laboratory in one response. */
    @Transactional(readOnly = true)
    public FullHistoryResponse getFullHistory(Long patientId) {
        Patient patient = patientService.findById(patientId);
        return new FullHistoryResponse(
                PatientResponse.of(patient, medicalHistoryService.getAllergies(patientId)),
                appointmentService.findByPatient(patientId),
                medicalHistoryService.findConsultations(patientId),
                prescriptionService.findByPatient(patientId),
                laboratoryService.findByPatient(patientId));
    }

    /** Patient + MedicalHistory (allergy check and record) + Prescription. */
    @Transactional
    public Prescription generatePrescription(Long patientId, List<MedicationRequest> medications) {
        patientService.findById(patientId);
        List<String> allergies = medicalHistoryService.getAllergies(patientId);
        rejectAllergicMedications(medications, allergies);
        Prescription prescription = prescriptionService.create(patientId, medications);
        String names = String.join(", ", medications.stream().map(MedicationRequest::name).toList());
        medicalHistoryService.recordConsultation(patientId, CLINIC_STAFF, "Prescription issued",
                "Medications: " + names);
        return prescription;
    }

    /** Patient + Laboratory + MedicalHistory. */
    @Transactional
    public List<LabResult> requestExams(Long patientId, List<ExamType> exams) {
        patientService.findById(patientId);
        List<LabResult> results = laboratoryService.requestExams(patientId, exams);
        long outOfRange = results.stream().filter(result -> !result.isWithinRange()).count();
        medicalHistoryService.recordConsultation(patientId, LABORATORY, "Lab exams requested",
                "Exams: " + exams.stream().distinct().map(Enum::name).toList()
                        + ". Parameters out of range: " + outOfRange);
        return results;
    }

    private void rejectAllergicMedications(List<MedicationRequest> medications, List<String> allergies) {
        for (MedicationRequest medication : medications) {
            String name = medication.name().toLowerCase(Locale.ROOT);
            for (String allergy : allergies) {
                if (name.contains(allergy.toLowerCase(Locale.ROOT))) {
                    throw new BusinessException(HttpStatus.UNPROCESSABLE_ENTITY,
                            "Patient is allergic to " + allergy + ": cannot prescribe " + medication.name());
                }
            }
        }
    }
}
