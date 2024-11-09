package com.example.plant_shop.exception;

import com.example.plant_shop.exception.customException.CommentException;
import com.example.plant_shop.exception.customException.FileFormatException;
import com.example.plant_shop.exception.customException.FileStorageException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.TreeMap;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({EntityExistsException.class})
    public ResponseEntity<Map<String, Object>> entityExistsException(EntityExistsException ex, HttpServletRequest request) {
        final Map<String, Object> body = new TreeMap<>();

        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("error", "EntityExistsException");
        body.put("message", ex.getMessage());
        body.put("details", "Измените данные в теле запроса.");
        body.put("path", request.getServletPath());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Map<String, Object>> entityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        final Map<String, Object> body = new TreeMap<>();

        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("error", "EntityNotFoundException");
        body.put("message", ex.getMessage());
        body.put("details", "Измените id или имя в запросе.");
        body.put("path", request.getServletPath());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CommentException.class})
    public ResponseEntity<Map<String, Object>> commentException(CommentException ex, HttpServletRequest request) {
        final Map<String, Object> body = new TreeMap<>();

        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("error", "CommentException");
        body.put("message", ex.getMessage());
        body.put("details", "Измените id комментария!");
        body.put("path", request.getServletPath());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({FileFormatException.class})
    public ResponseEntity<Map<String, Object>> fileFormatException(FileFormatException ex, HttpServletRequest request) {
        final Map<String, Object> body = new TreeMap<>();

        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("error", "FileFormatException");
        body.put("message", ex.getMessage());
        body.put("details", "Измените файл!");
        body.put("path", request.getServletPath());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({FileStorageException.class})
    public ResponseEntity<Map<String, Object>> fileStorageException(FileStorageException ex, HttpServletRequest request) {
        final Map<String, Object> body = new TreeMap<>();

        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        body.put("error", "FileStorageException");
        body.put("message", ex.getMessage());
        body.put("details", "Попробуйте позже.");
        body.put("path", request.getServletPath());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({PersistenceException.class})
    public ResponseEntity<Map<String, Object>> persistenceException(PersistenceException ex, HttpServletRequest request) {
        final Map<String, Object> body = new TreeMap<>();

        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        body.put("error", "PersistenceException");
        body.put("message", ex.getMessage());
        body.put("details", "Попробуйте позже.");
        body.put("path", request.getServletPath());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Map<String, Object>> unknownException(Exception ex, HttpServletRequest request) {
        final Map<String, Object> body = new TreeMap<>();

        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        body.put("error", "UnknownException");
        body.put("message", ex.getMessage());
        body.put("details", "Попробуйте позже.");
        body.put("path", request.getServletPath());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
