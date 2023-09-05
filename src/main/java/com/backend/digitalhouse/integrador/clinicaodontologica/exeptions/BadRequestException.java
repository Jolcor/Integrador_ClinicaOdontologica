package com.backend.digitalhouse.integrador.clinicaodontologica.exeptions;

public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
