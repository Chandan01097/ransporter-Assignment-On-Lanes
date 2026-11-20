package com.transporterLanes.exceptions;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
public class GlobalExceptionHandler {
	
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<Map<String, String>> handleIllegalState(
	        IllegalStateException ex) {

	    String message = ex.getMessage();

	    if (message != null && message.contains("within maxTransporters")) {
	        message = "Unable to assign all lanes using the requested maximum "
	                + "number of transporters. Please increase maxTransporters.";
	    } else {
	        message = "No valid transporter assignment is possible. "
	                + "Please check lane coverage and transporter quotations.";
	    }

	    return ResponseEntity
	            .status(HttpStatus.UNPROCESSABLE_ENTITY)
	            .body(Map.of(
	                "status", "FAILED",
	                "message", message
	            ));
	}
}
