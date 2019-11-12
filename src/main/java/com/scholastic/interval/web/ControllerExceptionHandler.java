package com.scholastic.interval.web;

import com.scholastic.interval.dto.ServerResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handle any Exception encountered during controller processing
 * by returning our custom DTO to the client.
 *
 * @author cingham
 */
@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class ControllerExceptionHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ServerResponseDto handleGenericException(Exception ex) {
		return new ServerResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.toString());
	}
}
