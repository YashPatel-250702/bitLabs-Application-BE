package com.talentstream.service;



import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class EventService {

	@Autowired
	private JavaMailSender mailSender;

	@Async
	@RabbitListener(queues = "notification.email.queue")
	public void sendNotification(String messages) {
		String from = "patelyash250702@gmail.com";
		String subject = "bitLabs Job Posting";
		String to = "mufasaking1100@gmail.com";

		SimpleMailMessage message = new SimpleMailMessage();
		String content = "Hey there!\n\n"
			    + "Exciting job opportunities just landed on bitLabs.\n"
				+messages+"\n"
			    + "Don't miss your chance — click below to explore now and apply today!\n\n"
			    + " https://jobs.bitlabs.in/candidate?\n\n"
			    + "Your next career move could be just a click away!";
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);

		mailSender.send(message);

		System.out.println("Notification sent successfully to: " + to);
		System.out.println("Notification sent for job: ");
	}
}
