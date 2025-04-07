package com.talentstream.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.talentstream.config.RabbitMQConfig;
import com.talentstream.dto.JobDTO;

@Service
public class EventService {

	@Autowired
	private JavaMailSender mailSender;
	@Async
	@RabbitListener(queues = RabbitMQConfig.QUEUE)
	public void sendNotification(JobDTO jobDTO) {
		
		
	    String from = "patelyash250702@gmail.com";
	    String subject = "New Job Posted In BitLabs";
	    String to = "godarshanreddy@gmail.com";

	    SimpleMailMessage message = new SimpleMailMessage();
	    String content = "Notification sent for job: " + jobDTO.getJobTitle() + 
	                     " at " + jobDTO.getCompanyname() + 
	                     " | Email: " + to;

	    message.setFrom(from);
	    message.setTo(to);
	    message.setSubject(subject);
	    message.setText(content);

	    mailSender.send(message);

	    System.out.println("Notification sent successfully to: " + to);
	    System.out.println("Notification sent for job: " + jobDTO.getJobTitle() + 
	                       " at " + jobDTO.getCompanyname() + 
	                       " | Email: " + jobDTO.getEmail());
	}

}
