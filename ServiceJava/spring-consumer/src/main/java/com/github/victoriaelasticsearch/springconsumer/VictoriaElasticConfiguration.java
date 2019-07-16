package com.github.victoriaelasticsearch.springconsumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class VictoriaElasticConfiguration {


    @Bean
    public TopicExchange zipkinTopic() {
        return new TopicExchange("zipkin-x", true, false);
    }

    @Bean
    public TopicExchange zipkinTopicDlx() {
        return new TopicExchange("zipkin-dlx", true, false);
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper){
        Jackson2JsonMessageConverter converter  = new Jackson2JsonMessageConverter(objectMapper);
        return converter;
    }


    @Bean
    public Queue workingQueue() {
        return QueueBuilder.durable("zipkin.q")
                .withArgument("x-dead-letter-exchange", zipkinTopicDlx().getName())
                .withArgument("x-dead-letter-routing-key", "dead-message")
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("zipkin.dlq")
                .withArgument("x-dead-letter-exchange", zipkinTopic().getName())
                .withArgument("x-message-ttl", 1000)
                .build();
    }

    @Bean
    public Binding workingBinding() {
        return BindingBuilder.bind(workingQueue()).to(zipkinTopic()).with("#");
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(zipkinTopicDlx()).with("#");
    }


}
