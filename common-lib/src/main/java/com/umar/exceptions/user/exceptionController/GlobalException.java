package com.umar.exceptions.user.exceptionController;

import com.umar.events.services.BatchValidationException;
import com.umar.exceptions.common.exception.ApiException;
import com.umar.exceptions.common.request.ApiError;
import com.umar.exceptions.common.response.ValidationErrorResponse;
import com.umar.exceptions.user.exception.ResourceAlreadyExistsException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalException {

    private final MessageSource messageSource;

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String,String>> handleUserExists(ResourceAlreadyExistsException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", exception.getLocalizedMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .code("VALIDATION_ERROR")
                .message("Any required field missing or format invalid")
                .response(
                        ValidationErrorResponse.builder()
                                .errors(errors)
                                .build()
                )
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiException(ApiException ex) {
        String message = messageSource.getMessage(ex.getMessage(), null, Locale.US);
        ApiError apiError = ApiError.builder()
                .status(ex.getStatus().value())
                .code(ex.getCode())
                .message(message)
                .response(
                        java.util.Map.of(
                                "error",
                                message
                        )
                )
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity
                .status(ex.getStatus())
                .body(apiError);
    }

    @ExceptionHandler(BatchValidationException.class)
    public ResponseEntity<Map<String, String>> handleBatchValidation(BatchValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getErrors());
    }
}
