package com.talentstream.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.talentstream.config.RabbitMQConfig;
import com.talentstream.entity.Job;
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

    @Scheduled(cron = "0 0 10 * * ?")
    @Async
    public void sendDateToQueue() {
        LocalDate yesterday = LocalDate.now().minusDays(1); 
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, yesterday.toString());
    }

    @Async
    @RabbitListener(queues = "notification.email.queue")
    public void sendNotification(String date) {
        LocalDate postedDate = LocalDate.parse(date);
        List<Job> jobsByDate = jobRepository.findByCreationDate(postedDate);

        if (jobsByDate.isEmpty()) {
            System.out.println("No jobs posted on " + postedDate);
            return;
        }

        List<String> applicantEmails = applicantRepository.findAllEmails();

        if (applicantEmails.isEmpty()) {
            System.out.println("No applicant emails found.");
            return;
        }

        StringBuilder contentBuilder = new StringBuilder("Hey there!\n\n");
        contentBuilder.append("Exciting job opportunities just landed on bitLabs (").append(postedDate).append("):\n\n");

        int jobNo = 1;
        for (Job job : jobsByDate) {
            contentBuilder
                .append(jobNo++).append(". ")
                .append(job.getJobTitle()).append(" at ")
                .append(job.getJobRecruiter().getCompanyname())
                .append(job.getJobURL())
                .append("\n");
        }

        contentBuilder.append("\nApply now: https://jobs.bitlabs.in/applicant-find-jobs\n\n")
                      .append("Your next career move could be just a click away!");

        
        for (String email : applicantEmails) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("patelyash250702@gmail.com");
            message.setTo(email);
            message.setSubject("bitLabs Job Posting");
            message.setText(contentBuilder.toString());

            mailSender.send(message);

            System.out.println("Notification sent successfully to: " + email);
        }

        System.out.println("Emails sent to " + applicantEmails.size() + " applicants.");
    }
}
