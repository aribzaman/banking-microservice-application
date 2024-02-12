package com.nagarro.accountservice.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nagarro.accountservice.dto.ResponseMessage;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * <b>{@link #resourceNotFoundException} </b> :: (Custom Exception for Not found in db ) <br><br>
 * <b>{@link #dataIntegrityViolationException}</b> -> SQLIntegrityConstraintViolationException  <br><br>
 * <b>{@link #feignException}</b> ::exceptions pertaining to feign <br><br>
 *
 * <b>{@link #handleRequestPathVariablesValidationException}</b> ConstraintViolationException :: For Validators in entity/records <br><br>
 * <b>{@link #handleMethodArgumentTypeMismatch}</b> MethodArgumentTypeMismatchException :: type conversion failure in the path variable of endpoint= 8080/customer/as OR giving null to int in queryParameter <br><br>
 * <b>{@link #handleMethodArgsNotValidException}</b> MethodArgumentNotValidException ::  For Validators in entity/records while persisting or @Validated <br><br>
 *
 * <b>{@link #handleHttpMessageNotReadableException} </b> httpMessageNotReadableException :: Redirection of wrong JSON form format to below functions <br><br>
 * 	 &emsp; <b>{@link #handleInvalidFormatException}</b> :: InvalidFormatException example= user : "a" // it should be 1 <br><br>
 * 	 &emsp; <b>{@link #handleJsonParseException}</b> :: InvalidFormatException example= user : "xyz" <br><br>
 *
 *  <b>{@link #missingPathVariableException}</b>  :: Path Variable absent so return 400 <br><br>
 * <b>{@link #missingServletRequestParameterException}</b>  ::  for missing query parameters <br><br>
 * <b>{@link #handleNotFoundError}</b> noResourceFoundException  :: Accessing unconfigured endpoints on server. <br><br>
 *
 */

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ResponseMessage> resourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
		log.error(ex.getClass().getSimpleName() + " :: "+ ex.getMessage());

		ResponseMessage message = new ResponseMessage(HttpStatus.NOT_FOUND
				, HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), ex.getMessage(), request.getRequestURI());

		return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ResponseMessage> dataIntegrityViolationException(DataIntegrityViolationException ex,
																		   HttpServletRequest request) {
		String response = ex.getClass().getSimpleName();
		if (ex.getMostSpecificCause() instanceof java.sql.SQLIntegrityConstraintViolationException cause){
			log.error(cause.getClass().getSimpleName() + " :: "+ cause.getMessage());
			response= cause.getMessage();
		}
		else {
			log.error(ex.getClass().getSimpleName() + " :: "+ ex.getMessage());
		}
		ResponseMessage message = new ResponseMessage(HttpStatus.CONFLICT, HttpStatus.CONFLICT.value(), LocalDateTime.now(),
				response, request.getRequestURI());

		return new ResponseEntity<>(message, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<ResponseMessage> feignException(FeignException ex, HttpServletRequest request) throws JsonProcessingException {
		ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
		if(ex.status() >= 500 && ex.status() <= 599){
			ResponseMessage errorResponse = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now(),
					"Server Unavailable at moment: " + ex.contentUTF8(), request.getRequestURI());
			return new ResponseEntity<>(errorResponse, HttpStatusCode.valueOf(ex.status()));
		}
		ResponseMessage errorResponse = mapper.readValue(ex.contentUTF8(), ResponseMessage.class);
		return new ResponseEntity<>(errorResponse, HttpStatusCode.valueOf(ex.status()));
	}

	@ExceptionHandler(ValidationFailedException.class)
	public ResponseEntity<ResponseMessage> validationFailedException(ValidationFailedException ex, HttpServletRequest request) {
		log.error(ex.getClass().getSimpleName() + " :: "+ ex.getMessage());
		ResponseMessage message = new ResponseMessage(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(),
				LocalDateTime.now(), ex.getMessage(), request.getRequestURI());

		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	private ResponseEntity<ResponseMessage> handleRequestPathVariablesValidationException(ConstraintViolationException ex,
																						  HttpServletRequest request) {
		Map<String, String> errors = new HashMap<>();
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			String propertyPath = violation.getPropertyPath().toString();
			String errorMessage = violation.getMessage();
			errors.put(propertyPath, errorMessage);
		}
		log.error(ex.getClass().getSimpleName() + " :: "+ errors);
		ResponseMessage message = new ResponseMessage(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(),
				errors.toString(), request.getRequestURI());
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ResponseMessage> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
																			HttpServletRequest request) {
		log.error(ex.getClass().getSimpleName() + " :: "+ ex.getMessage());
		ResponseMessage message = new ResponseMessage(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(),
				"Invalid data type for parameter: " + ex.getName(), request.getRequestURI());

		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

	//For Validators in entity/records
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseMessage> handleMethodArgsNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {

		Map<String, String> errors = new LinkedHashMap<String, String>();
		e.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			errors.put(fieldName, message);
		});

		ResponseMessage message = new ResponseMessage(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(),
				errors.toString(), request.getRequestURI());
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}
	
	//----------------

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ResponseMessage> handleHttpMessageNotReadableException(HttpMessageNotReadableException initialException,
																				 HttpServletRequest request) {
		Throwable ex = initialException.getCause();
		if (ex instanceof InvalidFormatException cause) {
			return handleInvalidFormatException(cause, request);}
		else if(ex instanceof JsonParseException cause){
			return handleJsonParseException(cause, request);}
		else {
			log.error(initialException.getClass().getSimpleName() + " :: "+ ex.getMessage());
			ResponseMessage message = new ResponseMessage(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(),
					initialException.getMessage(), request.getRequestURI());
			return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
		}
	}

	private ResponseEntity<ResponseMessage> handleInvalidFormatException(InvalidFormatException ex, HttpServletRequest request) {
		log.error(ex.getClass().getSimpleName() + " :: "+ ex.getMessage());
		ResponseMessage message = new ResponseMessage(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(),
				"Wrong data type provided in JSON Form with given value: " + ex.getValue(), request.getRequestURI());
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<ResponseMessage> handleJsonParseException(JsonParseException ex,
																	 HttpServletRequest request) {
		log.error(ex.getClass().getSimpleName() + " :: "+ ex.getMessage());
		ResponseMessage message = new ResponseMessage(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(),
				ex.getOriginalMessage(), request.getRequestURI());
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

//----------------------------------------------------------------

	@ExceptionHandler(MissingPathVariableException.class)
	public ResponseEntity<ResponseMessage> missingPathVariableException(MissingPathVariableException ex, HttpServletRequest request) {
		log.error(ex.getClass().getSimpleName() + " :: "+ ex.getMessage());
		ResponseMessage message = new ResponseMessage(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

	//--------------------  for missing query parameters
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ResponseMessage> missingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest request) {
		ResponseMessage message = new ResponseMessage(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}

	//--------------------- Accessing unconfigured endpoints on server
	@ExceptionHandler({NoResourceFoundException.class})
	public ResponseEntity<ResponseMessage> handleNotFoundError(NoResourceFoundException ex, HttpServletRequest request) {
		ResponseMessage message = new ResponseMessage(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), "The requested URL " + request.getRequestURI() + " was not found on this server.", null);
		return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
	}


//-------------------- Global Handler
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseMessage> globalExceptionHandler(Exception ex, HttpServletRequest request) {
		log.error("Fall Back Case of - " + ex.getClass().getSimpleName());
		log.error( ex.getMessage(), ex);

		ResponseMessage message = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR,
				HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now(), ex.getMessage() + " (Fall Back Case) ",
				request.getRequestURI());

		return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
