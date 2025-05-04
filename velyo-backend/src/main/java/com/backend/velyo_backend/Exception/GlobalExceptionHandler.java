package com.backend.velyo_backend.Exception;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.hibernate.TransientPropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.SignatureException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Collectors;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex){
        log.error("Recurso no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity<String> handleMalformedURLException(MalformedURLException ex) {
        log.error("URL invalido : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL invalido");
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<String> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex){
        log.error("El recurso ya existe : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex){
        log.error(ex.getMessage());
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Un error ocurrio mientras se procesaba el archivo");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex){
        log.error("Authentication failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email o contraseña inavlidas");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex){
        log.error("Argumento invalido: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredWjtException(ExpiredJwtException ex){
        log.error("Token expirado : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token Expirado");
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    public ResponseEntity<String> handleHttpMediaTypeException(HttpMediaTypeException ex) {
        log.error("Tipo de media invalido : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Tipo de media invalido");
    }
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.error("Tipo de media no soportado : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tipo de media no soportado");
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<String> handleSignatureException(SignatureException ex) {
        log.error("Firma de token invalida : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Firma de token invalida");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Violacion de Restricciones : {}", ex.getMessage());
        String errorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        log.error("Violación de la restricción de integridad de SQL : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Violación de la restricción de integridad de SQL");
    }

    @ExceptionHandler(TransientPropertyValueException.class)
    public ResponseEntity<String> handleTransientPropertyValueException(TransientPropertyValueException ex) {
        log.error("Valor de propiedad transitorio : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Valor de propiedad transitorio");
    }
}
