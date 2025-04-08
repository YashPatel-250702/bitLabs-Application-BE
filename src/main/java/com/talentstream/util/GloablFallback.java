package com.talentstream.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class GloablFallback {
	
	public ResponseEntity<String> globalFallback(Throwable t) {
	    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
	            .body("Too many requests. Please try again later.");
	}

}
