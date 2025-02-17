package org.happybaras.server.handlers;

import lombok.extern.slf4j.Slf4j;
import org.happybaras.server.domain.dtos.GeneralResponse;
import org.happybaras.server.utils.ErrorMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalErrorHandler {
    private final ErrorMapper errorMapper;

    public GlobalErrorHandler(ErrorMapper errorMapper) {
        this.errorMapper = errorMapper;
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<GeneralResponse> GeneralExceptionHandler (Exception e) {
        log.error(e.getMessage());
        log.error(e.getClass().toGenericString());
        return GeneralResponse.builder().
                status(HttpStatus.INTERNAL_SERVER_ERROR).
                getResponse();
    }

    @ExceptionHandler(NoResourceFoundException.class)
    private ResponseEntity<GeneralResponse> NotFoundExceptionHandler (NoResourceFoundException e) {
        return GeneralResponse.builder().
                status(HttpStatus.NOT_FOUND).
                message(e.getMessage()).
                getResponse();
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    private ResponseEntity<GeneralResponse> NotAuthorizedExceptionHandler(AuthorizationDeniedException e) {
        return GeneralResponse.builder().
                status(HttpStatus.FORBIDDEN).
                message(e.getMessage()).
                getResponse();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<GeneralResponse> BadRequestExceptionHandler(MethodArgumentNotValidException e) {
        return GeneralResponse.builder().
                status(HttpStatus.BAD_REQUEST).
                data(errorMapper.map(e.getBindingResult().getFieldErrors())).
                getResponse();
    }
}
