package com.marcio.newsletter_api.controllers.exceptions;

import com.marcio.newsletter_api.dtos.StandardErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardErrorDTO> errorTreatment(MethodArgumentNotValidException exception) {
        String mensagemErro = exception.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();

        StandardErrorDTO standardError = new StandardErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Dados de entrada inválidos.",
                mensagemErro
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);
    }
}
