package com.github.victoriaelasticsearch.servicespring;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessages(String message) {
        rabbitTemplate.convertAndSend("zipkin-exchange", "zipkin-victoria", message);
    }
}


