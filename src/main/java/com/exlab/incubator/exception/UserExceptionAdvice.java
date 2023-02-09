package com.exlab.incubator.exception;

import com.exlab.incubator.dto.responses.ExceptionDto;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserExceptionAdvice {

    @ExceptionHandler({FieldExistsException.class, UserNotFoundException.class})
    public ResponseEntity<ExceptionDto> handleException(RuntimeException e) {
        return new ResponseEntity<>(
            new ExceptionDto(e.getMessage(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> parameterExceptionHandler(MethodArgumentNotValidException e) {

        BindingResult result = e.getBindingResult();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                return new ResponseEntity<>(
                    new ExceptionDto(error.getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()),
                    HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(
            new ExceptionDto("Argument validation failed", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase()),
            HttpStatus.BAD_REQUEST);
    }

}
