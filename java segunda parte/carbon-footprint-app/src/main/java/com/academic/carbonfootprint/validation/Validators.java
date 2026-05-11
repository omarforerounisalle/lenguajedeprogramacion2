package com.academic.carbonfootprint.validation;

import java.util.Objects;

/**
 * Validaciones reutilizables (módulo de reglas) para evitar duplicar lógica en modelos.
 */
public final class Validators {

    public static final int MIN_YEAR = 1800;
    public static final int MAX_YEAR = 2100;

    private Validators() {
    }

    public static String requireNonBlank(String value, String fieldName) {
        String trimmed = value == null ? "" : value.trim();
        if (trimmed.isEmpty()) {
            throw new ValidationException(fieldName + " no puede estar vacío.");
        }
        return trimmed;
    }

    public static double requireNonNegative(double value, String fieldName) {
        if (value < 0) {
            throw new ValidationException(fieldName + " no puede ser negativo.");
        }
        return value;
    }

    public static int requireYearInRange(int year, String fieldName) {
        if (year < MIN_YEAR || year > MAX_YEAR) {
            throw new ValidationException(fieldName + " debe estar entre " + MIN_YEAR + " y " + MAX_YEAR + ".");
        }
        return year;
    }

    public static <T> T requireNonNull(T ref, String fieldName) {
        return Objects.requireNonNull(ref, fieldName + " es obligatorio.");
    }
}
