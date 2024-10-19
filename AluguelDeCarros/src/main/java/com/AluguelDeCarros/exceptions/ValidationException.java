package com.AluguelDeCarros.exceptions;

import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<String> validationErrors;

    public ValidationException(List<String> validationErrors) {
        super("Erro de validação: " + String.join("\n", validationErrors));
        this.validationErrors = validationErrors;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
