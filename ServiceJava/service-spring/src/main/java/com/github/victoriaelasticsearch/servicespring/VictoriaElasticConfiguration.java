package com.github.victoriaelasticsearch.servicespring;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VictoriaElasticConfiguration {

    static final String TOPIC_EXCHANGE = "zipkin-exchange";

    static final String QUEUE_NAME = "zipkin";
}
