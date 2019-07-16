package com.github.victoriaelasticsearch.springconsumer;

import com.github.victoriaelasticsearch.api.model.Trace;
import com.github.victoriaelasticsearch.api.model.TraceOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class Consumer {


    private Logger logger = LoggerFactory.getLogger(Consumer.class);

    @RabbitListener(queues = "zipkin.q")
    public void processMessage(Trace trace, @Header(required = false, name = "x-death") Map<?, ?> xDeath) {

        if(xDeath != null && (Long)xDeath.get("count") > 3) {
            throw new ImmediateAcknowledgeAmqpException("rejected Message");
        }

        Optional<TraceOption> traceOption = trace.getOptions()
                .stream()
                .filter(opt -> "service-spring-consumer".equals(opt.getServiceName()))
                .findFirst();

        if(traceOption.isPresent()) {

            this.logger.info("options are present");

            TraceOption option = traceOption.get();

            if(option.getDelay() > 0) {
                try {

                    Thread.sleep(option.getDelay().longValue());

                } catch (InterruptedException e) {
                    // do nothing
                }
            }

            if(option.getThrowException()) {
                throw new AmqpRejectAndDontRequeueException("zipkin exception");
            }


        }

        this.logger.info("trace successfully processed");

    }


}
