package com.talentstream.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;
import com.talentstream.entity.Applicant;
import com.talentstream.entity.UserFcmTokens;
import com.talentstream.exception.CustomException;
import com.talentstream.repository.ApplicantRepository;
import com.talentstream.repository.UserFcmTokensRepository;

@Service
public class FirebaseMessagingService {
	
	@Autowired
	private UserFcmTokensRepository fcmTokensRepsitory;
	@Autowired
	private ApplicantRepository applicantRepository;
	
	public void saveFcmTOken(UserFcmTokens fcmTokens,Long id) throws Exception {
		
		Applicant applicant = applicantRepository.findById(id).orElseThrow(()->new Exception("Applicant Not Found with id: "+id));
		  List<UserFcmTokens> userFcmTokenByDevice = fcmTokensRepsitory.findByApplicant_Id(id);
		  for(UserFcmTokens userFcmTokens:userFcmTokenByDevice) {
			  if(userFcmTokens.getFcmToken().equals(fcmTokens.getFcmToken())){
				  throw new CustomException("Same Token Already Present for Applicant", HttpStatus.BAD_REQUEST);
			  }
		  }
		fcmTokens.setApplicant(applicant);
		fcmTokens.setCreatedAt(LocalDateTime.now());
		fcmTokensRepsitory.save(fcmTokens);
	}
	
	public List<UserFcmTokens> getUserActiveFcmTokenById(Long id) {
		List<UserFcmTokens> fcmToken = fcmTokensRepsitory.findByApplicant_IdAndIsTokenActiveTrue(id);
	    if(fcmToken==null) {
	    	throw new CustomException( "Fcm Token Not found",HttpStatus.NOT_FOUND);
	    }
		return fcmToken;
	}

	public String sendNotification(Long id, String title, String body) throws Exception {
	    try {
	        List<UserFcmTokens> userFcmTokenByDevice = getUserActiveFcmTokenById(id);
	        if (userFcmTokenByDevice.isEmpty()) {
	            throw new CustomException("No Fcm Token found for applicant id: " + id, HttpStatus.NOT_FOUND);
	        }

	        Notification notification = Notification.builder()
	                .setTitle(title)
	                .setBody(body)                                                                                                                                                                                                                                                                                                                                                                                                                                          
	                .build(); 

	        StringBuilder result = new StringBuilder();

	        for (UserFcmTokens userFcmToken : userFcmTokenByDevice) {
	            String token = userFcmToken.getFcmToken();

	            Message message = Message.builder()
	                    .setToken(token)
	                    .setNotification(notification)
	                    .build();

	            try {
	                String response = FirebaseMessaging.getInstance().send(message);
	                result.append("Sent to token: ").append(token)
	                      .append(" => Response: ").append(response).append("\n");
	            } catch (FirebaseMessagingException fme) {
	                // Handle expired or invalid token
	                if (fme.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED ||
	                    fme.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT) {
	                    
	                	System.out.println(fme.getMessage());
	                	//making token expired in database
	                    userFcmToken.setIsTokenActive(false);
	                    fcmTokensRepsitory.save(userFcmToken);
	               }
	            }
	        }

	        return result.toString();

	    } catch (CustomException e) {
	        throw new CustomException(e.getMessage(), e.getStatus());
	    } catch (Exception e) { 
	        e.printStackTrace();
	        throw new Exception("Error while sending Notification");
	    }
	}
}
