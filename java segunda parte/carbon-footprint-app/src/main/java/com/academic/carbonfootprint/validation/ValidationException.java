package com.academic.carbonfootprint.validation;

/**
 * Señaliza entradas inválidas en dominio o UI sin mezclar con errores de E/S.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
