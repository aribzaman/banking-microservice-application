package com.nagarro.accountservice.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//ResourceNotFoundException.class :: (throw Manually Not found in db )
//DataIntegrityViolationException.class -> SQLIntegrityConstraintViolationException :: Repeated Value posted in db
//ConstraintViolationException.class :: For Validators in entity/records
//MethodArgumentTypeMismatchException.class :: type conversion failure in the path variable of endpoint= 8080/customer/as OR giving null to int in queryParameter
//HttpMessageNotReadableException.class :: Redirection of wrong JSON form format to below functions
//		handleInvalidFormatException(function) :: InvalidFormatException example= user : "a" // it should be 1 / "1"
//		handleJsonParseException (function)    :: InvalidFormatException example= user : asdasd
//MissingPathVariableException :: Path Variable absent so return 400

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorMessage> resourceNotFoundException(ResourceNotFoundException ex,
                                                                  HttpServletRequest request) {
		log.error(ex.getClass().getSimpleName() + " :: "+ ex.getMessage());
		ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND
				, HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), ex.getMessage(), request.getRequestURI());

		return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorMessage> DataIntegrityViolationException(DataIntegrityViolationException ex,
																HttpServletRequest request) {
		String response = ex.getClass().getSimpleName();
		if (ex.getMostSpecificCause() instanceof java.sql.SQLIntegrityConstraintViolationException cause){
			log.error(cause.getClass().getSimpleName() + " :: "+ cause.getMessage());
			response= "Duplicate entry for the given item";
		}
		ErrorMessage message = new ErrorMessage(HttpStatus.CONFLICT, HttpStatus.CONFLICT.value(), LocalDateTime.now(),
				response, request.getRequestURI());

		return new ResponseEntity<ErrorMessage>(message, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	private ResponseEntity<ErrorMessage> handleRequestPathVariablesValidationException(jakarta.validation.ConstraintViolationException ex,
			HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			String propertyPath = violation.getPropertyPath().toString();
			String errorMessage = violation.getMessage();
			errors.put(propertyPath, errorMessage);
		}
		log.error(ex.getClass().getSimpleName() + " :: "+ errors);
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(),
				errors.toString(), request.getRequestURI());
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorMessage> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			HttpServletRequest request) {
		log.error(ex.getClass().getSimpleName() + " :: "+ ex.getMessage());
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(),
				"Invalid data type for parameter: " + ex.getName(), request.getRequestURI());

		return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
	}
	
	//----------------

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorMessage> handleHttpMessageNotReadableException(HttpMessageNotReadableException initialException,
			HttpServletRequest request) {
		Throwable ex = initialException.getCause();
		if (ex instanceof InvalidFormatException cause) {
			return handleInvalidFormatException(cause, request);}
		else if(ex instanceof JsonParseException cause){
			return handleJsonParseException(cause, request);}
		else {
			log.error(initialException.getClass().getSimpleName() + " :: "+ ex.getMessage());
			ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(),
					initialException.getMessage(), request.getRequestURI());
			return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
		}
	}

	private ResponseEntity<ErrorMessage> handleInvalidFormatException(InvalidFormatException ex, HttpServletRequest request) {
		log.error(ex.getClass().getSimpleName() + " :: "+ ex.getMessage());
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(),
				"Wrong data type provided in JSON Form with given value: " + ex.getValue(), request.getRequestURI());
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<ErrorMessage> handleJsonParseException(JsonParseException ex,
			HttpServletRequest request) {
		log.error(ex.getClass().getSimpleName() + " :: "+ ex.getMessage());
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(),
				ex.getOriginalMessage(), request.getRequestURI());
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
	}

//----------------------------------------------------------------

	@ExceptionHandler(MissingPathVariableException.class)
	public ResponseEntity<ErrorMessage> MissingPathVariableException(MissingPathVariableException ex, HttpServletRequest request) {
		log.error(ex.getClass().getSimpleName() + " :: "+ ex.getMessage());
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST);
	}

//-------------------- Global Handler
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, HttpServletRequest request) {
		log.error("Fall Back Case of - " + ex.getClass().getSimpleName());
		log.error( ex.getMessage(), ex);

		ErrorMessage message = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
				HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now(), ex.getMessage() + " (Fall Back Case) ",
				request.getRequestURI());

		return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
