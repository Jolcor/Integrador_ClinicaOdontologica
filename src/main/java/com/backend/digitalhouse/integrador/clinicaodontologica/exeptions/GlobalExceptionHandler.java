package com.backend.digitalhouse.integrador.clinicaodontologica.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Esta annotation se usa para que la clase tenga la postea para manejar las excepciones
public class GlobalExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> manejarResourceNotFound(ResourceNotFoundException resourceNotFoundException) {
        Map<String, String> exceptionMessage = new HashMap<>();
        //agregamos con put
        exceptionMessage.put("messege", "Recurso no encontrado: " + resourceNotFoundException.getMessage());
        return exceptionMessage;
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> manejarBAdRequest(BadRequestException badRequestException) {
        Map<String, String> exceptionMessege = new HashMap<>();
        exceptionMessege.put("messege", "Error interno: " + badRequestException.getMessage());
        return exceptionMessege;
    }
}
