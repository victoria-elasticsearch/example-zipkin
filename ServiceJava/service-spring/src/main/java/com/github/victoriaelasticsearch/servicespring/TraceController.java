package com.github.victoriaelasticsearch.servicespring;

import com.github.victoriaelasticsearch.api.TraceApi;
import com.github.victoriaelasticsearch.api.model.Trace;
import com.github.victoriaelasticsearch.api.model.TraceOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
public class TraceController implements TraceApi {


    private Logger logger = LoggerFactory.getLogger(TraceController.class);


    private RabbitTemplate rabbitTemplate;

    public TraceController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public ResponseEntity<Trace> postTrace(UUID xRequestID, @Valid Trace trace) {
        this.logger.info("Received new trace");

        this.logger.info("trace: {}", trace);


        Optional<TraceOption> traceOption = trace.getOptions()
                .stream()
                .filter(opt -> "service-spring".equals(opt.getServiceName()))
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
                throw new InvalidDataAccessApiUsageException("zipkin exception");
            }


        }

        this.rabbitTemplate.convertAndSend(trace);

        return ResponseEntity.ok(trace);
    }

}
