package com.citi.hackathon;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Created by rd82846 on 10/18/2016.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public static final String UUID = "uuid";
    private HttpHeaders responseHeaders = new HttpHeaders();
    private ErrorResponse er = new ErrorResponse();

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity<?> handleException(HttpServletRequest req, Exception ex) {
        responseHeaders.set(UUID, req.getHeader("uuid"));
        er.setMessage(messageSource.getMessage("account.service.failed", null, Locale.US));
        er.setCode(Integer.parseInt(messageSource.getMessage("account.service.failed.code", null, Locale.US)));
        LOG.error("account.service.failed - " +ex);
        return new ResponseEntity<>(er, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    ResponseEntity<?> handleInvalidInput(HttpServletRequest req, MethodArgumentNotValidException ex) {
        final BindingResult bindingResult = ex.getBindingResult();
        final List<ObjectError> allErrors = bindingResult.getAllErrors();

        List<String> messages = new ArrayList<>();
        for (ObjectError allError : allErrors) {
            final String message = messageSource.getMessage(allError, Locale.US);
            messages.add(message);
        }
        final String combinedMessage = String.join("\n", messages);
        responseHeaders.set(UUID, req.getHeader("uuid"));
        er.setMessage(combinedMessage);
        final String code = messageSource.getMessage("invalid.user.input.code", null, Locale.US);
        er.setCode(Integer.parseInt(code));
        LOG.error("invalid.user.input - " +ex);
        return new ResponseEntity<>(er, responseHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    ResponseEntity<?> handleHttpMessageNotReadableException(HttpServletRequest req, HttpMessageNotReadableException ex) {
        responseHeaders.set(UUID, req.getHeader("uuid"));

        final String message = messageSource.getMessage("invalid.json.error", null, Locale.US);
        er.setMessage(message);
        final String code = messageSource.getMessage("invalid.json.error.code", null, Locale.US);
        er.setCode(Integer.parseInt(code));
        LOG.error("invalid.json.error - " +ex);
        return new ResponseEntity<>(er, responseHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    ResponseEntity<?> handleMethodArgumentTypeMismatchException(HttpServletRequest req, MethodArgumentTypeMismatchException ex) {
        responseHeaders.set(UUID, req.getHeader("uuid"));
        final String message = messageSource.getMessage("typeMismatch", new Object[]{ ex.getName()}, Locale.US);
        er.setMessage(message);
        final String code = messageSource.getMessage("typeMismatch.code", null, Locale.US);
        er.setCode(Integer.parseInt(code));
        LOG.error("typeMismatch - " +ex);
        return new ResponseEntity<>(er, responseHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    ResponseEntity<?> handleDataAccessException(HttpServletRequest req, DataAccessException ex) {
        responseHeaders.set(UUID, req.getHeader("uuid"));
        final String message = messageSource.getMessage("generic.database.error", null, Locale.US);
        LOG.error(message, ex);
        er.setMessage(message);
        final String code = messageSource.getMessage("generic.database.error.code", null, Locale.US);
        er.setCode(Integer.parseInt(code));
        LOG.error("generic.database.error - " +ex);
        return new ResponseEntity<>(er, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }
   


}