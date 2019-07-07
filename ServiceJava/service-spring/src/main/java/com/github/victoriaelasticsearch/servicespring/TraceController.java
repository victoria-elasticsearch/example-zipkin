package com.github.victoriaelasticsearch.servicespring;

import com.github.victoriaelasticsearch.api.TraceApi;
import com.github.victoriaelasticsearch.api.model.Trace;
import com.github.victoriaelasticsearch.api.model.TraceReceivedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
public class TraceController implements TraceApi {


    private Logger logger = LoggerFactory.getLogger(TraceController.class);

    @Override
    public ResponseEntity<Trace> postTrace(UUID xRequestID, @Valid Trace trace) {
        this.logger.info("Received new trace");
        return ResponseEntity.ok(trace);
    }

}
