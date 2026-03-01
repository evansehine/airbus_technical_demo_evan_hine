package com.example.satellites.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    // 1) Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", Instant.now(),
                        "status", 400,
                        "error", "Validation Failed",
                        "errors", errors
                ));
    }

    // 2) JSON parse / unreadable message - generic and reflection-friendly
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleJsonParseError(HttpMessageNotReadableException ex) {
        String message = "Malformed JSON request";
        Throwable cause = ex.getMostSpecificCause();

        if (cause != null) {
            // Try to build a friendlier message by reflection (works across different Jackson repackagings)
            try {
                // Attempt to extract value, path and targetType via common method names used by Jackson's InvalidFormatException
                Object value = invokeIfExists(cause, "getValue");
                Object pathObj = invokeIfExists(cause, "getPath");
                Object targetType = invokeIfExists(cause, "getTargetType");

                String path = null;
                if (pathObj != null) {
                    // getPath typically returns a List of Reference objects; join their field names if possible
                    try {
                        @SuppressWarnings("unchecked")
                        List<Object> pathList = (List<Object>) pathObj;
                        path = pathList.stream().map(p -> {
                            try {
                                Method m = p.getClass().getMethod("getFieldName");
                                Object fn = m.invoke(p);
                                return fn != null ? fn.toString() : p.toString();
                            } catch (Exception e) {
                                return p.toString();
                            }
                        }).collect(Collectors.joining("."));
                    } catch (Exception e) {
                        path = pathObj.toString();
                    }
                }

                String invalidValue = value != null ? value.toString() : null;
                String expectedType = (targetType != null) ? targetType.toString() : null;

                if (invalidValue != null && path != null && expectedType != null) {
                    message = String.format("Invalid value '%s' for field '%s'. Expected type: %s",
                            invalidValue, path, expectedType);
                } else if (cause.getMessage() != null) {
                    message = cause.getMessage();
                }
            } catch (Exception reflectionEx) {
                // reflection failed — fall back to the raw cause message
                if (cause.getMessage() != null) {
                    message = cause.getMessage();
                }
            }
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", Instant.now(),
                        "status", 400,
                        "error", "Bad Request",
                        "message", message
                ));
    }

    // 3) Not found
    @ExceptionHandler(SatelliteNotFoundException.class)
    public ResponseEntity<?> handleNotFound(SatelliteNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "timestamp", Instant.now(),
                        "status", 404,
                        "error", "Not Found",
                        "message", ex.getMessage()
                ));
    }

    // 4) Fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", Instant.now(),
                        "status", 500,
                        "error", "Internal Server Error",
                        "message", ex.getMessage()
                ));
    }

    // Helper: attempt to call a no-arg method by name on an object and return the result (or null)
    private static Object invokeIfExists(Object target, String methodName) {
        if (target == null) return null;
        try {
            Method m = target.getClass().getMethod(methodName);
            return m.invoke(target);
        } catch (Exception e) {
            return null;
        }
    }
}