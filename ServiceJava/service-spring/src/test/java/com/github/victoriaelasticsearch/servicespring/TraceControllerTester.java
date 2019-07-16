package com.github.victoriaelasticsearch.servicespring;

import com.github.victoriaelasticsearch.api.model.Trace;
import com.github.victoriaelasticsearch.api.model.TraceOption;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TraceControllerTester {

    @Mock
    private RabbitTemplate template;

    private TraceController traceController;

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);

        Mockito.doNothing().when(template).convertAndSend(Mockito.any());

        traceController = new TraceController(template);

    }

    @Test
    public void Test() {

        TraceOption option = new TraceOption();
        option.setServiceName("service-spring");
        option.setDelay(5);
        option.setThrowException(false);

        List<TraceOption> options = new ArrayList<TraceOption>();
        options.add(option);

        Trace trace = new Trace();
        trace.setOptions(options);


        traceController.postTrace(UUID.randomUUID(), trace);



    }



}
