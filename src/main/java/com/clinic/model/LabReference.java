package com.clinic.model;

/**
 * Reference range of a single lab parameter.
 */
public record LabReference(String parameter, String unit, double min, double max) {
}
