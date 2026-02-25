package com.argenischacon.inventory_sales_system.exception;

import com.argenischacon.inventory_sales_system.dto.error.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex,
                        HttpServletRequest request) {
                log.warn("Resource not found: {} [Path: {}]", ex.getMessage(), request.getRequestURI());
                ErrorResponseDTO error = ErrorResponseDTO.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.NOT_FOUND.value())
                                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                                .message(ex.getMessage())
                                .path(request.getRequestURI())
                                .build();
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler({ ResourceAlreadyExistsException.class, ResourceInUseException.class })
        public ResponseEntity<ErrorResponseDTO> handleConflictExceptions(RuntimeException ex,
                        HttpServletRequest request) {
                log.warn("Conflict exception: {} [Path: {}]", ex.getMessage(), request.getRequestURI());
                ErrorResponseDTO error = ErrorResponseDTO.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.CONFLICT.value())
                                .error(HttpStatus.CONFLICT.getReasonPhrase())
                                .message(ex.getMessage())
                                .path(request.getRequestURI())
                                .build();
                return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getAllErrors().forEach(error -> {
                        String fieldName = ((FieldError) error).getField();
                        String errorMessage = error.getDefaultMessage();
                        errors.put(fieldName, errorMessage);
                });

                log.warn("Validation failed: {} [Path: {}]", errors, request.getRequestURI());
                ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error("Validation Error")
                                .message("Invalid input parameters")
                                .path(request.getRequestURI())
                                .validationErrors(errors)
                                .build();
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(
                        DataIntegrityViolationException ex,
                        HttpServletRequest request) {
                log.error("Data integrity violation: {} [Path: {}]", ex.getMessage(), request.getRequestURI());
                ErrorResponseDTO error = ErrorResponseDTO.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.CONFLICT.value())
                                .error("Data Integrity Violation")
                                .message("Database error: Please check for duplicate entries or constraints violations.")
                                .path(request.getRequestURI())
                                .build();
                return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(ConstraintViolationException ex,
                        HttpServletRequest request) {
                log.warn("Constraint violation: {} [Path: {}]", ex.getMessage(), request.getRequestURI());
                ErrorResponseDTO error = ErrorResponseDTO.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error("Constraint Violation")
                                .message(ex.getMessage())
                                .path(request.getRequestURI())
                                .build();
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(
                        HttpMessageNotReadableException ex,
                        HttpServletRequest request) {
                log.warn("Malformed JSON request: {} [Path: {}]", ex.getMessage(), request.getRequestURI());
                ErrorResponseDTO error = ErrorResponseDTO.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error("Malformed JSON")
                                .message("Request body is missing or malformed.")
                                .path(request.getRequestURI())
                                .build();
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatchException(
                        MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
                log.warn("Type mismatch: Parameter '{}' expected '{}' [Path: {}]", ex.getName(), ex.getRequiredType(),
                                request.getRequestURI());
                ErrorResponseDTO error = ErrorResponseDTO.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error("Type Mismatch")
                                .message(String.format("Parameter '%s' must be of type '%s'", ex.getName(),
                                                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName()
                                                                : "unknown"))
                                .path(request.getRequestURI())
                                .build();
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(InsufficientStockException.class)
        public ResponseEntity<ErrorResponseDTO> handleInsufficientStockException(InsufficientStockException ex,
                        HttpServletRequest request) {
                log.warn("Insufficient stock: {} [Path: {}]", ex.getMessage(), request.getRequestURI());
                ErrorResponseDTO error = ErrorResponseDTO.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error("Insufficient Stock")
                                .message(ex.getMessage())
                                .path(request.getRequestURI())
                                .build();
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(BadCredentialsException ex,
                        HttpServletRequest request) {
                log.warn("Authentication failed: {} [Path: {}]", ex.getMessage(), request.getRequestURI());
                ErrorResponseDTO error = ErrorResponseDTO.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                                .message("Invalid username or password.")
                                .path(request.getRequestURI())
                                .build();
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(AccessDeniedException ex,
                        HttpServletRequest request) {
                log.warn("Access denied: {} [Path: {}]", ex.getMessage(), request.getRequestURI());
                ErrorResponseDTO error = ErrorResponseDTO.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.FORBIDDEN.value())
                                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                                .message("You do not have permission to access this resource.")
                                .path(request.getRequestURI())
                                .build();
                return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception ex, HttpServletRequest request) {
                log.error("Unexpected error occurred [Path: {}]", request.getRequestURI(), ex);
                ErrorResponseDTO error = ErrorResponseDTO.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                                .message("An unexpected error occurred processing your request.")
                                .path(request.getRequestURI())
                                .build();
                return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
}
