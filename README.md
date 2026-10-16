# Clinic Management Backend — Facade Pattern

REST backend for a small medical clinic built with Java 21 and Spring Boot 3. It registers patients, books appointments, issues prescriptions, simulates lab exams, and returns a patient's full medical history.

## Facade pattern

`ClinicFacade` (`com.clinic.facade`) is the only entry point to the business logic. `ClinicController` calls the facade and never a service directly. The five subsystems never call each other: each one uses only its own repositories, and the facade coordinates them.

| Subsystem | Responsibility |
|---|---|
| `PatientService` | Registers patients (unique document number) and looks them up |
| `AppointmentService` | Lists doctors, computes free slots (next 5 business days, 08:00–11:00 every hour), books and cancels appointments, builds reminders |
| `MedicalHistoryService` | Records consultations and stores patient allergies |
| `PrescriptionService` | Creates and lists prescriptions |
| `LaboratoryService` | Simulates HEMOGRAM, GLUCOSE and LIPID_PROFILE results with reference ranges |

What each facade operation coordinates:

| Facade method | Subsystems used |
|---|---|
| `registerPatient` | Patient + MedicalHistory (allergies) |
| `findPatientByDocument` | Patient + MedicalHistory (allergies) |
| `findDoctors` | Appointment |
| `scheduleAppointment` | Patient (exists) + Appointment (book + reminder) + MedicalHistory (records the consultation) |
| `getFullHistory` | Patient + Appointment + MedicalHistory + Prescription + Laboratory |
| `generatePrescription` | Patient + MedicalHistory (allergy check → 422) + Prescription + MedicalHistory (records the consultation) |
| `requestExams` | Patient + Laboratory + MedicalHistory (records the consultation) |

## Endpoints

Base path: `/api/clinica`. Errors always return `{"message": "..."}` with status 400, 404, 409 or 422.

| Method | Path | Description |
|---|---|---|
| GET | `/health` | `{"status":"UP"}` |
| POST | `/paciente` | Register a patient → 201 |
| GET | `/paciente/{documentNumber}` | Find a patient by document (login) |
| GET | `/medicos?especialidad=CARDIOLOGY` | Doctors with available slots |
| POST | `/cita` | Book an appointment → 201 `{appointment, reminder}` |
| GET | `/historia/{patientId}` | Full consolidated history |
| POST | `/prescripcion` | Issue a prescription → 201 (422 on allergy) |
| POST | `/laboratorio` | Request lab exams → 201 list of results |

Specialties: `GENERAL_MEDICINE`, `CARDIOLOGY`, `PEDIATRICS`. Exams: `HEMOGRAM`, `GLUCOSE`, `LIPID_PROFILE`.

On startup, `DataInitializer` inserts five doctors only if the doctor table is empty. No other data is seeded.

## Environment variables

| Variable | Description |
|---|---|
| `DB_HOST` | PostgreSQL host |
| `DB_PORT` | PostgreSQL port |
| `DB_NAME` | Database name |
| `DB_USER` | Database user |
| `DB_PASSWORD` | Database password |
| `PORT` | HTTP port (default 8080, set by Railway) |

## Run

```bash
./mvnw spring-boot:run
```

Or build the Docker image with the included `Dockerfile`. The app is deployed on Railway from the `main` branch.

Production URL: `https://<railway-domain>/api/clinica/health`
