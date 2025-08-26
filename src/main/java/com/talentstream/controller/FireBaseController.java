package com.talentstream.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentstream.dto.NotificationDto;
import com.talentstream.entity.UserFcmTokens;
import com.talentstream.exception.CustomException;
import com.talentstream.service.FirebaseMessagingService;

@RestController
@RequestMapping("/notification")
public class FireBaseController {

   
	@Autowired
	private FirebaseMessagingService firebaseMessagingService;
	
	@PostMapping("/saveFcmToken/{id}")
	public ResponseEntity<?> saveFcmToken(@Valid @RequestBody UserFcmTokens fcmTokens,BindingResult bindingResult, @PathVariable Long id){
		if(bindingResult.hasErrors()) {
			Map<String, String> errors=new HashMap<>();
			bindingResult.getFieldErrors().forEach(err->{
				errors.put(err.getField(), err.getDefaultMessage());
			});
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
		try {
			
			firebaseMessagingService.saveFcmTOken(fcmTokens,id);
			return ResponseEntity.status(HttpStatus.OK).body("Fcm Token Saved Successfully");
		}
		catch (CustomException e) {
			System.err.println(e.getMessage());
			return ResponseEntity.status(e.getStatus()).body(e.getMessage());
		}
		catch (Exception e) { 
			System.err.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
		}
	}
	
	@PostMapping("/send/{id}")
	public ResponseEntity<?> sendNotification(@Valid @RequestBody NotificationDto dto ,BindingResult bindingResult,@PathVariable Long id){
		if(bindingResult.hasErrors()) {
			Map<String, String> errors=new HashMap<>();
			bindingResult.getFieldErrors().forEach(err->{
				errors.put(err.getField(), err.getDefaultMessage());
			});
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
		
		try {
			String sendNotification = firebaseMessagingService.sendNotification(id,dto.getTitle(),dto.getBody());
			System.out.println(sendNotification);                   
			if(sendNotification.isBlank()) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token is not valid or expired Please try again with correct FCM Token");
			}
			return ResponseEntity.status(HttpStatus.OK).body("Notification sent succefully");
		} 
		catch (CustomException e) {
			System.err.println(e.getMessage());
			return ResponseEntity.status(e.getStatus()).body(e.getMessage());
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
		}
	}

}
