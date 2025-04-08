package com.talentstream.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.talentstream.config.RabbitMQConfig;
import com.talentstream.dto.JobDTO;

@Service
public class NotificationProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Async
    public void sendToQueue(String message) throws InterruptedException {
    	  System.out.println("Sending message to RabbitMQ: " + message);
    	  Thread.sleep(5000);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, message);
    }
}
