package org.example.recapproject12.exceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.example.recapproject12.exceptions.IdNotFound;
import org.example.recapproject12.exceptions.MissingDataToConstructToDo;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IdNotFound.class)
    public ResponseEntity<String> idNotFoundHandler(IdNotFound e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<String> InvalidMediaTypeExceptionHandler(InvalidFormatException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect Value entered: " + e.getValue());
    }

    @ExceptionHandler(MissingDataToConstructToDo.class)
    public ResponseEntity<String> MissingDataToConstructToDo(MissingDataToConstructToDo e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }


}