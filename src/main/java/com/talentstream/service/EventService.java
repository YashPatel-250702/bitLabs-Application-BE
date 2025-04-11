package com.talentstream.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.talentstream.config.RabbitMQConfig;
import com.talentstream.dto.ApplicantNotificationDTO;
import com.talentstream.entity.ApplicantProfile;
import com.talentstream.entity.Job;
import com.talentstream.repository.ApplicantProfileRepository;
import com.talentstream.repository.ApplicantRepository;
import com.talentstream.repository.JobRepository;

@Service
public class EventService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private ApplicantRepository applicantRepository;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private ApplicantProfileRepository applicantProfileRepository;

	@Scheduled(cron = "0 0 10 * * ?")
	@Async
	public void sendDateToQueue() {
		
		LocalDate yesterday = LocalDate.now().minusDays(1);
		List<Job> jobsByDate = jobRepository.findByCreationDate(yesterday);
		
		if (jobsByDate.isEmpty()) {
			System.out.println("No Job Posted Yesterday");
			return;
		}

		List<ApplicantNotificationDTO> applicants = applicantRepository.findAllApplicantIdAndEmails();
		if (applicants.isEmpty()) {
			System.out.println("No Applicant present");
			return;
		}

		for (ApplicantNotificationDTO applicant : applicants) {
			
			ApplicantProfile profile = applicantProfileRepository.findByApplicantId(applicant.getId());
			
			if (profile == null) continue;

			Set<String> applicantSkills = profile.getSkillsRequired().stream()
				.map(skill -> skill.getSkillName().toLowerCase())
				.collect(Collectors.toSet());

			List<Job> matchingJobs = jobsByDate.stream().filter(job -> {
				Set<String> jobSkills = job.getSkillsRequired().stream()
					.map(skill -> skill.getSkillName().toLowerCase())
					.collect(Collectors.toSet());
				
				jobSkills.retainAll(applicantSkills);
				return !jobSkills.isEmpty();
				
			}).collect(Collectors.toList());

			if (matchingJobs.isEmpty()) continue;

			StringBuilder content = new StringBuilder("Hey there!\n\n")
				.append("Exciting job opportunities matching your skills posted on ")
				.append(yesterday).append(":\n\n");

			int jobNo = 1;
			for (Job job : matchingJobs) {
				content.append(jobNo++).append(". ").append(job.getJobTitle())
					.append(" at ").append(job.getJobRecruiter().getCompanyname())
					.append(" - ").append(job.getJobURL()).append("\n");
			}

			content.append("\nApply now: https://jobs.bitlabs.in/applicant-find-jobs\n\n")
				.append("Your next career move could be just a click away!");

			Map<String, String> message = new HashMap<>();
			message.put("to", applicant.getEmail());
			message.put("content", content.toString());

			rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, message);
		}
	}

	@Async
	@RabbitListener(queues = "notification.email.queue")
	public void sendNotification(Map<String, String> notificationInfo) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("patelyash250702@gmail.com");
		message.setTo(notificationInfo.get("to"));
		message.setSubject("bitLabs Job Posting");
		message.setText(notificationInfo.get("content"));
		mailSender.send(message);
		System.out.println("Notification Sent Successfully To : "+notificationInfo.get("to"));
	}
}
