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
	private UserFcmTokensRepository fcmTokensRepository;

	@Autowired
	private ApplicantRepository applicantRepository;

	public void saveFcmToken(UserFcmTokens fcmTokens, Long id) throws Exception {
		Applicant applicant = applicantRepository.findById(id)
				.orElseThrow(() -> new Exception("Applicant Not Found with id: " + id));

		List<UserFcmTokens> existingTokens = fcmTokensRepository.findByApplicant_Id(id);
		for (UserFcmTokens existing : existingTokens) {
			if (existing.getFcmToken().equals(fcmTokens.getFcmToken())) {
				throw new CustomException("Same Token Already Present for Applicant", HttpStatus.BAD_REQUEST);
			}
		}

		fcmTokens.setApplicant(applicant);
		fcmTokens.setCreatedAt(LocalDateTime.now());
		fcmTokensRepository.save(fcmTokens);
	}

	public List<UserFcmTokens> getUserActiveFcmTokenById(Long id) {
		List<UserFcmTokens> fcmTokens = fcmTokensRepository.findByApplicant_IdAndIsTokenActiveTrue(id);
		if (fcmTokens == null || fcmTokens.isEmpty()) {
			throw new CustomException("Fcm Token Not found", HttpStatus.NOT_FOUND);
		}
		return fcmTokens;
	}

	public String sendNotification(Long applicantId, String title, String body) throws Exception {
		List<UserFcmTokens> tokens = getUserActiveFcmTokenById(applicantId);
		return sendToTokens(tokens, title, body);
	}

	public String sendNotificationToAll(String title, String body) throws Exception {
		List<UserFcmTokens> tokens = fcmTokensRepository.findAll();
		if (tokens.isEmpty()) {
			throw new CustomException("No Fcm Tokens found", HttpStatus.NOT_FOUND);
		}
		return sendToTokens(tokens, title, body);
	}

	private String sendToTokens(List<UserFcmTokens> userFcmTokens, String title, String body) throws Exception {

		Notification notification = Notification.builder().setTitle(title).setBody(body).build();

		StringBuilder result = new StringBuilder();

		for (UserFcmTokens userFcmToken : userFcmTokens) {
			String token = userFcmToken.getFcmToken();

			Message message = Message.builder().setToken(token).setNotification(notification).build();

			try {
				String response = FirebaseMessaging.getInstance().send(message);
				result.append("Sent to token: ").append(token).append(" => Response: ").append(response).append("\n");
			} catch (FirebaseMessagingException fme) {
				if (fme.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED
						|| fme.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT) {

					System.out.println("Deactivating invalid token: " + userFcmToken.getFcmToken());
					userFcmToken.setIsTokenActive(false);
					fcmTokensRepository.save(userFcmToken);
				}
			}
		}
		return result.toString();
	}

}
