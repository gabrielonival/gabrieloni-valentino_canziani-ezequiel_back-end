package com.backend.integrador.controller.advice;

import com.backend.integrador.dto.error.ErrorRespuestaDTO;
import com.backend.integrador.exception.BadRequestException;
import com.backend.integrador.exception.ResourceNotFoundException;
import com.backend.integrador.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.validation.FieldError;

@RestControllerAdvice
public class GenericControllerAdvice extends ResponseEntityExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(GenericControllerAdvice.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> badRequestException(BadRequestException badRequestException){
        String messageException = badRequestException.getMessage();

        ErrorRespuestaDTO respuesta = new ErrorRespuestaDTO(
                messageException,
                DateUtils.obtenerFechaHoraActualFormateada(),
                HttpStatus.BAD_REQUEST);

        LOGGER.error("badRequestException: {}", messageException);
        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException resourceNotFoundException){
        String messageException = resourceNotFoundException.getMessage();

        ErrorRespuestaDTO respuesta = new ErrorRespuestaDTO(
                messageException,
                DateUtils.obtenerFechaHoraActualFormateada(),
                HttpStatus.NOT_FOUND);

        LOGGER.error("resourceNotFoundException: {}", messageException);
        return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errores = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        Map<String, Object> responseBody = Map.of(
                "message", "Validación fallida",
                "errores", errores,
                "timestamp", DateUtils.obtenerFechaHoraActualFormateada(),
                "status", HttpStatus.BAD_REQUEST

        );
        LOGGER.error("error trying validate request body: ", ex);
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }
}
