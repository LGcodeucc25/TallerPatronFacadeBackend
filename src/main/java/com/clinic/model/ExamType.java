package com.clinic.model;

import java.util.List;

/**
 * Supported lab exams and the reference ranges of the parameters they measure.
 */
public enum ExamType {
    HEMOGRAM(List.of(
            new LabReference("hemoglobin", "g/dL", 12, 17.5),
            new LabReference("white blood cells", "x10^3/uL", 4.5, 11),
            new LabReference("platelets", "x10^3/uL", 150, 450))),
    GLUCOSE(List.of(
            new LabReference("fasting glucose", "mg/dL", 70, 100))),
    LIPID_PROFILE(List.of(
            new LabReference("total cholesterol", "mg/dL", 0, 200),
            new LabReference("HDL", "mg/dL", 40, 60),
            new LabReference("LDL", "mg/dL", 0, 100),
            new LabReference("triglycerides", "mg/dL", 0, 150)));

    private final List<LabReference> references;

    ExamType(List<LabReference> references) {
        this.references = references;
    }

    public List<LabReference> getReferences() {
        return references;
    }
}
