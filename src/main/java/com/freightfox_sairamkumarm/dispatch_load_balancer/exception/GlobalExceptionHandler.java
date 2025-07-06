package com.freightfox_sairamkumarm.dispatch_load_balancer.exception;

import com.freightfox_sairamkumarm.dispatch_load_balancer.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Map<String, String>>> handleValidationError(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        for(FieldError fieldError: ex.getBindingResult().getFieldErrors()){
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>("Invalid input", "failed", errors);
        return ResponseEntity.badRequest().body(responseDTO);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDTO<String>> handleEnumParseErrors(HttpMessageNotReadableException ex) {
        ResponseDTO<String> response = new ResponseDTO<>("error", "Invalid input: check enums or malformed JSON.", null);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<String>> handleUnknownErrors(Exception ex) {
//        ex.printStackTrace();
        ResponseDTO<String> response = new ResponseDTO<>(
                "error", "Unexpected error occurred", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
